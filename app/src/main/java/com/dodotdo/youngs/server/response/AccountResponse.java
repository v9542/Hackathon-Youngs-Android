package com.dodotdo.youngs.server.response;

import com.dodotdo.youngs.data.Account;
import com.dodotdo.youngs.data.Question;

import java.util.List;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class AccountResponse {
    List<Account> results;

    public List<Account> getResult() {
        return results;
    }
}
