package com.dodotdo.youngs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.bumptech.glide.Glide;
import com.dodotdo.youngs.R;
import com.dodotdo.youngs.adapter.HistoryAdapter;
import com.dodotdo.youngs.data.History;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.request.LoginRequest;
import com.dodotdo.youngs.server.response.HistoryResponse;
import com.dodotdo.youngs.server.response.LectureResponse;
import com.dodotdo.youngs.server.response.LoginResponse;
import com.dodotdo.youngs.view.MyRecyclerView;
import com.dodotdo.youngs.walkietalkie.GetJsonRequest;
import com.dodotdo.youngs.walkietalkie.Recorder;
import com.dodotdo.youngs.walkietalkie.WalkieTalkieSocket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HistoryActivity extends AppCompatActivity {

    MyRecyclerView list;
    HistoryAdapter adapter;

    List<History> data;

    int channelId;
    String token;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getChannelIdAndTitle();
        getToken();
        initializeActionBar();
        getHistory();
        initialize();
    }

    private void initialize() {
        list = (MyRecyclerView)findViewById(R.id.question_list);
    }

    private void setListData() {
        adapter = new HistoryAdapter(this, data);

        list.setAdapter(adapter);
    }

    private void initializeActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialMenuDrawable drawable = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        drawable.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setNavigationIcon(drawable);
        ((TextView)findViewById(R.id.tv_title)).setText(title);
    }

    private void getToken() {
        SharedPreferences pref = getSharedPreferences("store", 0);
        token = pref.getString("token", "");
    }


    private void getHistory() {
        ServerQuery.getHistory(channelId, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if(message.equals("OK")) {
                    HistoryResponse historyResponse = (HistoryResponse) response.body();
                    data = historyResponse.getResults();
                    setListData();
                }else {
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    private void getChannelIdAndTitle() {
        Intent i = getIntent();
        channelId = i.getIntExtra("channel_id", 1);
        title = i.getStringExtra("title");
    }
}
