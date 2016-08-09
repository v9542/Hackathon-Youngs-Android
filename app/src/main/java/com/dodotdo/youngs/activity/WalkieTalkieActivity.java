package com.dodotdo.youngs.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.bumptech.glide.Glide;
import com.dodotdo.youngs.R;
import com.dodotdo.youngs.Util.Util;
import com.dodotdo.youngs.data.Account;
import com.dodotdo.youngs.data.Lecture;
import com.dodotdo.youngs.data.Question;
import com.dodotdo.youngs.data.Voice;
import com.dodotdo.youngs.server.ServerAPI;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.ServiceGenerator;
import com.dodotdo.youngs.server.request.LoginRequest;
import com.dodotdo.youngs.server.response.AccountResponse;
import com.dodotdo.youngs.server.response.HistoryResponse;
import com.dodotdo.youngs.server.response.LectureResponse;
import com.dodotdo.youngs.server.response.LoginResponse;
import com.dodotdo.youngs.server.response.QuestionResponse;
import com.dodotdo.youngs.walkietalkie.GetJsonRequest;
import com.dodotdo.youngs.walkietalkie.Recorder;
import com.dodotdo.youngs.walkietalkie.WalkieTalkieSocket;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WalkieTalkieActivity extends AppCompatActivity {

    RoundedImageView myProfileView, otherProfileView;
    Button recordView, nextQuestionView;
    TextView questionNumView, questionView;

    byte[] mData, receivedVoice;
    short[] buffer;
    Recorder recorder;

    boolean isRecording = false;
    int channelId;
    String token;
    int teacherId;
    String title;
    List<Question> question;
    int nowQuesionIndex;

    WalkieTalkieSocket socket;
    GetJsonRequest jsonRequest;

    Handler updateChannelInfo;
    Activity thisActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkietalkie);

        getToken();
        getChannelId();
        initialize();
        requestAttend();
        initializeActionBar();
        initializeUpdateChannelInfo();
    }

    private void initializeUpdateChannelInfo() {

        updateChannelInfo.postDelayed(new Runnable() {
            @Override
            public void run() {
                getListener();
                updateChannelInfo.postDelayed(this, 5000);
            }
        }, 3000);
    }

    private void getListener() {
        ServerQuery.getListener(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                AccountResponse accountResponse = (AccountResponse)response.body();
                List<Account> listener = accountResponse.getResult();
                String message = response.message();
                if (message.equals("OK")) {
                    try {
                        if (listener.size() == 2) {
                            Glide.with(WalkieTalkieActivity.this).load(listener.get(0).getProfile_url()).into(myProfileView);
                            Glide.with(WalkieTalkieActivity.this).load(listener.get(1).getProfile_url()).into(otherProfileView);
                        } else if(listener.size() == 1){
                            if(listener.get(0).getId()!=teacherId) {
                                requestOutClass();
                                Toast.makeText(WalkieTalkieActivity.this, "수업이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WalkieTalkieActivity.this, MainActivity.class));
                                MainActivity.activity.finish();
                                thisActivity.finish();
                            }else {
                                Glide.with(WalkieTalkieActivity.this).load("http://192.168.1.100/static/white_bg.jpeg").into(otherProfileView);
                            }
                        }
                    }catch(IllegalArgumentException e) {
                        Log.d("Yebon", "Already closed");
                    }
                } else {
                    Toast.makeText(WalkieTalkieActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[getChannelInfo]");
            }
        });
    }

    private void initializeActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialMenuDrawable drawable = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        drawable.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setNavigationIcon(drawable);
    }

    private void getToken() {
        SharedPreferences pref = getSharedPreferences("store", 0);
        token = pref.getString("token", "");
    }

    private void setLectureData(Lecture lecture) {
        if (lecture.getListener() != null) {
            Glide.with(this).load(lecture.getListener().getProfile_url()).into(otherProfileView);
        }
        Glide.with(this).load(lecture.getMember().getProfile_url()).into(myProfileView);
        title = lecture.getTitle();
        teacherId = lecture.getMember().getId();
        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    private void getChannelInfo() {
        ServerQuery.getLectureInfo(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {
                    LectureResponse lectureResponse = (LectureResponse) response.body();
                    Lecture data = lectureResponse.getResults();
                    setLectureData(data);
                    getQuestion(data.getType());
                } else {
                    Toast.makeText(WalkieTalkieActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[getChannelInfo]");
            }
        });
    }

    private void getQuestion(String type) {
        ServerQuery.getQuestion(type, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {
                    QuestionResponse questionResponse = (QuestionResponse) response.body();
                    question = questionResponse.getResult();
                    setQuestion();
                } else {
                    Toast.makeText(WalkieTalkieActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[getChannelInfo]");
            }
        });
    }

    private void setQuestion() {
        if (question.size() == 0) {
            return;
        }
        questionNumView.setText("Q" + (nowQuesionIndex + 1) + ".");
        questionView.setText(question.get(nowQuesionIndex).getContent());
    }

    private void getChannelId() {

        channelId = getIntent().getIntExtra("channel_id", 1);
    }


    private void initialize() {
        myProfileView = (RoundedImageView) findViewById(R.id.my_profile);
        otherProfileView = (RoundedImageView) findViewById(R.id.other_profile);
        recordView = (Button) findViewById(R.id.record);
        nextQuestionView = (Button) findViewById(R.id.next_question);
        questionNumView = (TextView) findViewById(R.id.question_num);
        questionView = (TextView) findViewById(R.id.question);

        recordView.setOnTouchListener(touchListener);
        nextQuestionView.setOnClickListener(clickListener);

        socket = new WalkieTalkieSocket(this, mHandler, channelId, token);
        socket.start();
        recorder = new Recorder();

        jsonRequest = new GetJsonRequest(this);

        nowQuesionIndex = 0;
        updateChannelInfo = new Handler();
        thisActivity=this;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.next_question:
                    if (nowQuesionIndex == question.size() - 1) {
                        nowQuesionIndex = 0;
                    } else {
                        nowQuesionIndex++;
                    }
                    setQuestion();
                    break;
            }
        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    occupyChannel();
                    break;
                case MotionEvent.ACTION_UP:
                    recordView.setBackground(getResources().getDrawable(R.drawable.speak_1));
                    isRecording = false;
                    break;

            }
            return false;
        }
    };

    protected void occupyChannel() {
        ServerQuery.occupyChannel(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {
                    recordView.setBackground(getResources().getDrawable(R.drawable.speak_2));
                    recording();
                } else if(message.equals("CONFLICT")) {
                    Toast.makeText(WalkieTalkieActivity.this, "상대방이 말하고 있어요!", Toast.LENGTH_SHORT).show();
                } else if(message.equals("FORBIDDEN")) {
                    Toast.makeText(WalkieTalkieActivity.this, "학생이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    protected void releaseChannel() {
        ServerQuery.releaseChannel(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {
                } else {
                    Toast.makeText(WalkieTalkieActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    protected void recording() {
        isRecording = true;
        if (isRecording) {
            Log.d("Yebon", "recording start!");
            new RecordingTasker().execute("");
        }
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
        receivedVoice = (byte[]) voice.getData();
        recorder.playing(receivedVoice, voice.getLength());
    }

    private void requestOutClass() {
        ServerQuery.outLecture(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {

                } else {
                    Toast.makeText(WalkieTalkieActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    private void requestAttend() {
        ServerQuery.attendLecture(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if (message.equals("OK")) {
                    getChannelInfo();
                } else {
                    Toast.makeText(getApplicationContext(), "이미 수업이 진행중이거나 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    @Override
    protected void onDestroy() {
        recorder.stopPlaying();
        socket.disConnect();
        requestOutClass();
        super.onDestroy();
    }

    public class RecordingTasker extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            recorder.startRecording();
            buffer = new short[Recorder.bufferSize];

            while (isRecording) {
                try {
                    socket.setIsSaying(true);
                    mData = recorder.record(buffer);
                    socket.sendVoice(mData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            socket.setIsSaying(false);

            recorder.stopRecording();
            releaseChannel();
            return null;
        }
    }

    protected void makeEndDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 종료하시겠습니까?")
                .setTitle(title)
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }
                )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        requestOutClass();
                        startActivity(new Intent(WalkieTalkieActivity.this, MainActivity.class));
                        MainActivity.activity.finish();
                        thisActivity.finish();
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        makeEndDialog();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home || id == android.R.id.home) {
            onBackPressed();
        }


        return super.onOptionsItemSelected(item);
    }
}
