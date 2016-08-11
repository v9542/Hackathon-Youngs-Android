package com.dodotdo.youngs.server;

/**
 * Created by Omjoon on 16. 2. 1..
 */
public class NetDefine {
    public static final String PATH = "192.168.10.3";
    public static final int SOCKET_PORT = 8040;
    public static final int DEFAULT_PORT = 80;
    public static final int WEB_PORT = 80;

    public static String getBasicPath() {
        return "http://"+PATH+":"+DEFAULT_PORT;
    }


    public static String getWebPath() {
        return "http://"+PATH+":"+WEB_PORT;
    }

    public static String getSocketPath() {
        return PATH;
    }


}
