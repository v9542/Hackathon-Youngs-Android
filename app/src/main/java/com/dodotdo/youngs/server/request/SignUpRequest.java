package com.dodotdo.youngs.server.request;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class SignUpRequest {
    String email;
    String password;
    String nickname;
    String profile_img;

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SignUpRequest(String email, String password, String nickname, String image) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profile_img = image;

    }
}
