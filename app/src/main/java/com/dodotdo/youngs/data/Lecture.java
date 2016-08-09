package com.dodotdo.youngs.data;

import java.util.List;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class Lecture {
    int id;
    String title;
    String description;
    String img_url;
    String register_timestamp;
    String type;
    Account listener;
    Account member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRegister_timestamp() {
        return register_timestamp;
    }

    public void setRegister_timestamp(String register_timestamp) {
        this.register_timestamp = register_timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Account getListener() {
        return listener;
    }

    public void setListener(Account listener) {
        this.listener = listener;
    }

    public Account getMember() {
        return member;
    }

    public void setMember(Account member) {
        this.member = member;
    }
}
