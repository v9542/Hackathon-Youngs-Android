package com.dodotdo.youngs.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dodotdo.youngs.R;
import com.dodotdo.youngs.data.Voice;
import com.dodotdo.youngs.walkietalkie.Recorder;
import com.dodotdo.youngs.walkietalkie.WalkieTalkieSocket;

import java.io.IOException;
import java.util.Arrays;

import io.kvh.media.amr.AmrDecoder;
import io.kvh.media.amr.AmrEncoder;

/**
 * Created by KimYebon on 16. 8. 9..
 */
public class TestActivity extends AppCompatActivity {
    byte[] mData, receivedVoice;
    short[] buffer;
    Recorder recorder;
    int encodingMode;
    long decodingState;

    boolean isRecording = false;

    WalkieTalkieSocket socket;
    Button record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkietalkie);

        record = (Button)findViewById(R.id.record);

        socket = new WalkieTalkieSocket(this, mHandler, 1, "sdfs");
        socket.start();
        recorder = new Recorder();
        AmrEncoder.init(0);
        encodingMode = AmrEncoder.Mode.MR122.ordinal();
        decodingState = AmrDecoder.init();

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordingTasker().execute("");
                isRecording=true;
            }
        });
    }

    //socket핸들러
    Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                case WalkieTalkieSocket.STATE_CONNECTED:
                    Log.d("TEST", "start");
                    break;
                case WalkieTalkieSocket.STATE_DISCONNECTED:
                    Log.d("TEST", "end");
                    break;
                case WalkieTalkieSocket.RECEIVE_VOICE:
                    Log.d("TEST", "receive_voice");
                    receiveVoice(inputMessage);
                    break;
                default:
                    Log.d("TEST", "Invalid Socket Message");
                    break;

            }
        }
    };

    protected void receiveVoice(Message inputMessage) {
        Voice voice = (Voice)inputMessage.obj;
        receivedVoice = voice.getData();
        recorder.playing(receivedVoice, voice.getLength());
    }

    public class RecordingTasker extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            recorder.startRecording();
            buffer = new short[Recorder.bufferSize];
            mData = new byte[Recorder.bufferSize/5];

            while (isRecording) {
                try {
                    mData = recorder.record(buffer);
//                    socket.sendVoice(mData);
                    Log.d("Yebon", "before encoding : " + Arrays.toString(buffer));
                    AmrEncoder.encode(encodingMode, buffer, mData);
                    AmrDecoder.decode(decodingState, mData, buffer);
                    Log.d("Yebon", "after decoding : " + Arrays.toString(buffer));
                    recorder.playing(buffer, buffer.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            recorder.stopRecording();
            return null;
        }
    }
}
