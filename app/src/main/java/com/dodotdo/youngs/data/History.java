package com.dodotdo.youngs.data;

import java.io.File;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class History {
    int id;
    Account member;
    String mp3File_url;

    public Account getMember() {
        return member;
    }

    public void setMember(Account member) {
        this.member = member;
    }

    public String getMp3File_url() {
        return mp3File_url;
    }

    public void setMp3File(String mp3File_url) {
        this.mp3File_url = mp3File_url;
    }
}
