package actors;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.Kill;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import messages.ForSearchRequest;
//import scala.concurrent.duration.Duration;
import messages.WebRequest;
import messages.WebResponse;
import scala.concurrent.duration.Duration;
import search.Searcher;

import static java.util.List.of;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MasterActor extends UntypedActor {

    private static final String YANDEX_NAME   = "yandex";
    private static final String GOOGLE_NAME   = "google";
    private static final String MAILRU_NAME   = "mailru";
    private static final int    SEARCHERS_NUM = 3;
    private              String query;

    private final Map<String, List<String>> results      = new HashMap<>(SEARCHERS_NUM);
    private final LinkedList<ActorRef>      searchActors = new LinkedList<>(
        of(context().actorOf(Props.create(ChildActor.class), YANDEX_NAME),
            context().actorOf(Props.create(ChildActor.class), GOOGLE_NAME),
            context().actorOf(Props.create(ChildActor.class), MAILRU_NAME)
        )
    );
    private final LinkedList<Searcher>      searchers    = new LinkedList<>(
        of(new Searcher(YANDEX_NAME, true),
            new Searcher(GOOGLE_NAME),
            new Searcher(MAILRU_NAME)
        )
    );

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WebRequest) {
            query = ((WebRequest) message).getQuery();
            for (int i = 0; i < SEARCHERS_NUM; i++) {
                searchActors.get(i).tell(new ForSearchRequest(query, searchers.get(i)), getSelf());
            }
            getContext().setReceiveTimeout(Duration.apply(1, SECONDS));
        }

        if (message instanceof WebResponse) {
            results.put(getSender().path().name(), ((WebResponse) message).getResults());
            if (results.size() == SEARCHERS_NUM) {
                printResultsAndStop();
            }
        }

        if (message instanceof ReceiveTimeout) {
            printResultsAndStop();
        }

        unhandled(message);
    }

    private void printResultsAndStop() {
        System.out.println("Query: " + query);

        results.forEach((searcher, answers) -> {
            System.out.println(searcher.toUpperCase() + ": ");
            answers.forEach(System.out::println);
        });
        context().system().terminate();
    }
}
