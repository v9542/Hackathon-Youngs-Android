package com.dodotdo.youngs.data;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class Account {
    int id;
    String nickname;
    String email;
    String profile_url;
    String recent_login_timestamp;
    String register_timestamp;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_img(String profile_img) {
        this.profile_url = profile_img;
    }
}
