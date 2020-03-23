package messages;

import java.io.Serializable;
import java.util.List;

public class WebResponse implements Serializable {

    private final List<String> results;

    public WebResponse(List<String> results) {
        this.results = results;
    }

    public List<String> getResults() {
        return results;
    }

}
