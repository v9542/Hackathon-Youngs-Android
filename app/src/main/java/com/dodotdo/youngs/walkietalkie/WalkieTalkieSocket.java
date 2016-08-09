package com.dodotdo.youngs.walkietalkie;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dodotdo.youngs.data.Voice;
import com.dodotdo.youngs.server.NetDefine;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.response.QuestionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by KimYebon on 16. 7. 25..
 */
public class WalkieTalkieSocket extends Thread {
    private Socket mSocket;

    private BufferedReader reader;
    private BufferedWriter bufferedWriter;
    private BufferedOutputStream voiceWriter;
    private BufferedInputStream voiceReader;
    private PrintWriter writer;

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
    GetJsonRequest jsonRequest;
    String token;

    public WalkieTalkieSocket(Context context, Handler mHandler, int channelId, String token) {
        mAddr = NetDefine.PATH;
        mPort = NetDefine.SOCKET_PORT;
        this.mHandler = mHandler;
        this.channelId = channelId;
        jsonRequest = new GetJsonRequest(context);
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

            SocketAddress socketAddress = new InetSocketAddress(addr, port);
            mSocket = new Socket();

            mSocket.connect(socketAddress, timeout);
            mSocket.setTcpNoDelay(false);

            reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
            voiceWriter = new BufferedOutputStream(mSocket.getOutputStream());
            voiceReader = new BufferedInputStream(mSocket.getInputStream());
            writer = new PrintWriter(bufferedWriter, true);
            mConnected = true;
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void sendAuth() {

        send(token);
    }

    @Override
    public void run() {

        if (!connect(mAddr, mPort, 20000))
            return;

        if (mSocket == null) return;

        mHandler.sendMessage(makeMessage(STATE_CONNECTED, ""));

        sendAuth();

        receivedVoice = new byte[8192];
        Voice voice;
        while (!Thread.interrupted()) {
            try {
                int length = voiceReader.read(receivedVoice, 0, 8192);
                voice = new Voice(receivedVoice, length);
                Log.d("Yebon", length+"received"+Arrays.toString(receivedVoice));
                mHandler.sendMessage(makeMessage(RECEIVE_VOICE, voice));
            } catch (IOException e1) {
            }
        }
    }


    public boolean disConnect() {
        if (!mConnected)
            return false;

        try {
            reader.close();
            writer.close();
            mConnected = false;
            mHandler.sendMessage(makeMessage(STATE_DISCONNECTED, ""));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isConnected() {
        return mConnected;
    }

    public void send(Object data) {
        if (!mConnected) {
            Log.d("Yebon", "socket is not connected");
            return;
        }
        writer.println(data);
        writer.flush();
    }

    public void sendVoice(byte[] data) {
        if (!mConnected) {
            Log.d("Yebon", "socket is not connected");
            return;
        }
        try {
            voiceWriter.write(data, 0, 8192);
            Log.d("Yebon", data.length+"send"+Arrays.toString(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}