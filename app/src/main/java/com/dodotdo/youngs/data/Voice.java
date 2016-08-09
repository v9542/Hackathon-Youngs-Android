package com.dodotdo.youngs.data;

/**
 * Created by KimYebon on 16. 8. 6..
 */
public class Voice {
    byte[] data;
    int length;

    public Voice(byte[] data, int length) {
        this.data = data;
        this.length = length;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
