package com.dodotdo.youngs.walkietalkie;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dodotdo.youngs.data.Voice;
import com.dodotdo.youngs.server.NetDefine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Created by KimYebon on 16. 7. 25..
 */
public class UDPSocket extends Thread {
    private DatagramSocket mSocket;
    private DatagramPacket receivedPacket;

    private String mAddr;
    private int mPort;
    private int channelId;
    private static boolean mConnected = false;
    private Handler mHandler = null;

    private byte[] receivedVoice;

    public static final int STATE_CONNECTED = 1;
    public static final int STATE_DISCONNECTED = 2;
    public static final int RECEIVE_VOICE = 7;

    protected boolean isSaying;
    String token;

    public UDPSocket(Handler mHandler, int channelId, String token) {
        mAddr = NetDefine.PATH;
        mPort = NetDefine.SOCKET_PORT;
        this.mHandler = mHandler;
        this.channelId = channelId;
        this.token = token;
    }


    public void setIsSaying(boolean isSaying) {
        this.isSaying = isSaying;
    }

    public boolean getIsSaying() {
        return this.isSaying;
    }

    protected Message makeMessage(int what, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }

    protected boolean connect(String addr, int port, int timeout) {
        try {
            if (mConnected)
                return true;


            mSocket = new DatagramSocket();

            mConnected = true;
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void run() {

        if (!connect(mAddr, mPort, 20000))
            return;

        if (mSocket == null) return;

        mHandler.sendMessage(makeMessage(STATE_CONNECTED, ""));

        //sendAuth();

        receivedVoice = new byte[32];
        receivedPacket = new DatagramPacket(receivedVoice, receivedVoice.length);
        while (!Thread.interrupted()) {
            try {
                mSocket.receive(receivedPacket);
                receivedVoice = receivedPacket.getData();
                Log.d("Yebon", receivedVoice.length + "received" + Arrays.toString(receivedVoice));
                mHandler.sendMessage(makeMessage(RECEIVE_VOICE, receivedVoice));
            } catch (NullPointerException e1) {

            } catch(IOException e2) {
                e2.printStackTrace();
            }
        }
    }


    public boolean disConnect() {
        if (!mConnected)
            return false;

        mConnected = false;
        mHandler.sendMessage(makeMessage(STATE_DISCONNECTED, ""));

        return true;
    }

    public boolean isConnected() {
        return mConnected;
    }


    public void sendVoice(byte[] data) {
        if (!mConnected) {
            Log.d("Yebon", "socket is not connected");
            return;
        }
        DatagramPacket packet;
        InetAddress serverAddr= null;
        try {
            serverAddr = InetAddress.getByName(mAddr);
        }catch(UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            packet = new DatagramPacket(data, data.length, serverAddr, mPort);
            mSocket.send(packet);
            Log.d("Yebon", data.length + "send" + Arrays.toString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}