package actors;

import java.util.stream.IntStream;

import akka.actor.UntypedActor;
import messages.ForSearchRequest;
import messages.WebResponse;
import search.Searcher;

public class ChildActor extends UntypedActor {

    @Override
    public void onReceive(Object o) throws Exception {
        if (o instanceof ForSearchRequest) {
            String query = ((ForSearchRequest) o).getQuery();
            Searcher searcher = ((ForSearchRequest) o).getSearcher();

            getSender().tell(new WebResponse(searcher.search(query)), getSelf());
        } else unhandled(o);
    }
}
