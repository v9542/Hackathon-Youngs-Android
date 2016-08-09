package com.dodotdo.youngs.data;

import java.io.File;
import java.util.List;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class Question {
    String content;
    int id;
    String type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
