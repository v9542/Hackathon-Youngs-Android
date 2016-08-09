package com.dodotdo.youngs.server.response;

/**
 * Created by Omjoon on 16. 2. 1..
 */
public class BasicResponse {
    String result;
    public BasicResponse(String result){
        this.result = result;

    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
