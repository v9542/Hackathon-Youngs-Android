package com.dodotdo.youngs.server.response;

import com.dodotdo.youngs.data.Login;
import com.dodotdo.youngs.data.Question;

import java.util.List;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class QuestionResponse {
    List<Question> results;

    public List<Question> getResult() {
        return results;
    }
}
