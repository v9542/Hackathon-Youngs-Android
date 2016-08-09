package com.dodotdo.youngs.data;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class Login {
    String email;
    String token;
    int id;
    String nickname;
    String profile_url;
    String recent_login_timestamp;
    String register_timestamp;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getRecent_login_timestamp() {
        return recent_login_timestamp;
    }

    public void setRecent_login_timestamp(String recent_login_timestamp) {
        this.recent_login_timestamp = recent_login_timestamp;
    }

    public String getRegister_timestamp() {
        return register_timestamp;
    }

    public void setRegister_timestamp(String register_timestamp) {
        this.register_timestamp = register_timestamp;
    }
}
