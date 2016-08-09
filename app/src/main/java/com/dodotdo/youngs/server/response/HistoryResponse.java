package com.dodotdo.youngs.server.response;

import com.dodotdo.youngs.data.Account;
import com.dodotdo.youngs.data.History;

import java.util.List;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class HistoryResponse {
    List<History> results;

    public List<History> getResults() {
        return results;
    }

    public void setResults(List<History> results) {
        this.results = results;
    }
}
