package com.dodotdo.youngs.server.request;

/**
 * Created by KimYebon on 16. 8. 7..
 */
public class ChannelRequest {
    String title;
    String description;
    String type;
    String img;

    public ChannelRequest(String title, String description, String type, String img) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.img = img;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
