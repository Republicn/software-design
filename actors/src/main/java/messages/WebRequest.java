package messages;

import java.io.Serializable;

public class WebRequest implements Serializable {

    private String query;

    public WebRequest(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
