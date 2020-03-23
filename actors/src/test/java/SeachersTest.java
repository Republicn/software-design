import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import search.Searcher;

import static java.util.List.of;
import static org.junit.Assert.assertEquals;

public class SeachersTest {

    String query = "Hello";

    @Test
    public void notStickyTest() throws InterruptedException {
        Searcher     searcher = new Searcher("google");
        List<String> results  = searcher.search(query);
        List<String> expectedResults = new LinkedList<>(of("google 1 result on Hello request",
            "google 2 result on Hello request",
            "google 3 result on Hello request",
            "google 4 result on Hello request",
            "google 5 result on Hello request"));

        assertEquals(results.size(), 5);
        assertEquals(results, expectedResults);
    }

    @Test
    public void stickyTest() throws InterruptedException {
        Searcher     searcher = new Searcher("google", true);
        List<String> results  = searcher.search(query);
        List<String> expectedResults = new LinkedList<>(of("google 1 result on Hello request",
            "google 2 result on Hello request",
            "google 3 result on Hello request",
            "google 4 result on Hello request",
            "google 5 result on Hello request"));

        assertEquals(results.size(), 5);
        assertEquals(results, expectedResults);
    }

}
