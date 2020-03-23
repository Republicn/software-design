import org.junit.Test;

public class SearchAggregatorTest {

    @Test
    public void testSearchAggregator() {
        SearchAggregator aggregator = new SearchAggregator();

        aggregator.find("Hello");
    }

}
