import actors.MasterActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import messages.WebRequest;

import static akka.actor.ActorRef.noSender;

public class SearchAggregator {

    private static final String ACTORS_SYSTEM_NAME = "ActorsSystem";
    private static final String SUPERVISOR_NAME = "Supervisor";

    private static ActorSystem system = ActorSystem.create(ACTORS_SYSTEM_NAME);

    public void find(String request) {
        ActorRef supervisor = system.actorOf(Props.create(MasterActor.class), SUPERVISOR_NAME);

        supervisor.tell(new WebRequest(request), noSender());
    }

}
