package messages;

import search.Searcher;

public class ForSearchRequest {
    private String   query;
    private Searcher searcher;

    public ForSearchRequest(String query, Searcher searcher) {
        this.query = query;
        this.searcher = searcher;
    }

    public String getQuery() {
        return query;
    }

    public Searcher getSearcher() {
        return searcher;
    }
}
