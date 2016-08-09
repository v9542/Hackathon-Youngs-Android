package com.dodotdo.youngs.server;

/**
 * Created by Omjoon on 16. 2. 1..
 */
public class NetDefine {
    public static final String PATH = "52.78.28.223";
    public static final int SOCKET_PORT = 9000;
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
