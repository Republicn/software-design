package search;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

public class Searcher {

    private String name;
    private boolean sticky = false;

    public Searcher(String name) {
        this.name = name;
    }

    public Searcher(String name, boolean isSticky) {
        this.name = name;
        this.sticky = isSticky;
    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public List<String> search(String query) throws InterruptedException {
        if (sticky) SECONDS.sleep(10);
        return IntStream.rangeClosed(1, 5)
            .boxed()
            .map(ind -> name + " " + ind + " result on " + query + " request")
            .collect(toList());
    }
}
