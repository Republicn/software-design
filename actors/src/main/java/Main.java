public class Main {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SearchAggregator aggregator = new SearchAggregator();
        aggregator.find("Hello");
        System.out.println(System.currentTimeMillis() - start);
    }

}
