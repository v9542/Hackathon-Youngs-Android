package com.dodotdo.youngs.server.request;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class LoginRequest {
    String email;
    String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
