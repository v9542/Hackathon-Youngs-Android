package com.dodotdo.youngs.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.dodotdo.youngs.R;
import com.dodotdo.youngs.Util.Util;
import com.dodotdo.youngs.server.request.ChannelRequest;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.request.SignUpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MakeChannelActivity extends AppCompatActivity {

    EditText titleView, descriptionView;
    Spinner typeView;
    ImageView coverView;
    Button makeChannelView;

    String token;

    ArrayAdapter adapter;

    final int photoCode = 9999;

    String path;
    File file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_channel);

        getToken();
        initializeActionBar();
        initialize();
    }

    private void initialize() {
        titleView = (EditText)findViewById(R.id.title_edit);
        descriptionView = (EditText)findViewById(R.id.description_edit);
        typeView = (Spinner)findViewById(R.id.type_spinner);
        coverView = (ImageView)findViewById(R.id.image_view);
        makeChannelView = (Button)findViewById(R.id.make_channel);
        adapter = ArrayAdapter.createFromResource(this, R.array.type_list, android.R.layout.simple_spinner_item);
        typeView.setAdapter(adapter);
        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(Intent.ACTION_PICK);
                cameraIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                cameraIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, photoCode);
            }
        });
        makeChannelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(collectData());
            }
        });
    }

    protected String getType(String type) {
        if(type.equals("OPIC")) {
            return "OPIC";
        }else if(type.equals("토플")) {
            return "TOEFL";
        }else if(type.equals("토익")) {
            return "TOEIC";
        }else if(type.equals("회사 면접")) {
            return "INTERVIEW";
        }else if(type.equals("자유주제")) {
            return "FREE";
        }else {
            return "LIFE";
        }
    }

    protected ChannelRequest collectData() {
        String title = titleView.getText().toString();
        String description = descriptionView.getText().toString();
        String type = getType(typeView.getSelectedItem().toString());
        String img = imageToString();

        ChannelRequest request;

        if(title.equals("") || title.equals(" ")) {
            Util.makeDialogAndShow("오류", "제목을 입력해주세요.", MakeChannelActivity.this);
            return null;
        }
        if(description.equals("") || description.equals(" ")) {
            Util.makeDialogAndShow("오류", "설명을 입력해주세요.", MakeChannelActivity.this);
            return null;
        }
        request = new ChannelRequest(title, description, type, img);

        return request;
    }

    protected String imageToString() {
        byte[] buf = null;
        RandomAccessFile f = null;
        String encodeBase64String = null;

        if(file==null)
            return "";

        try {
            f = new RandomAccessFile(file, "r");
            long longLength = f.length();
            int length = (int) longLength;
            if (length != longLength)
                throw new IOException("File size >= 2 GB");
            buf = new byte[length];
            f.readFully(buf);
            encodeBase64String = Base64.encodeToString(buf, Base64.DEFAULT);
            encodeBase64String = "data:image/jpeg;base64,"+encodeBase64String;
            Log.d("Yebon", encodeBase64String);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                f.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return encodeBase64String;

    }

    public void sendData(ChannelRequest request) {
        if(request==null)
            return;

        ServerQuery.makeChannel(request, token, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if(message.equals("OK")) {
                    MainActivity.activity.finish();
                    startActivity(new Intent(MakeChannelActivity.this, MainActivity.class));
                    finish();
                }else if(message.equals("NOT ACCEPTABLE")){
                    Util.makeDialogAndShow("오류", "양식을 지키고 사진을 등록해주세요.", MakeChannelActivity.this);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO 중복 확인
                Log.d("Yongs", t.getMessage() + "[makeChannel]");
            }
        });

    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == photoCode) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    BitmapDrawable image = new BitmapDrawable(getResources(), image_bitmap);
                    coverView.setBackgroundDrawable(image);
                    path = getRealPathFromURI(data.getData());
                    file = new File(path);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialMenuDrawable drawable = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        drawable.setIconState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setNavigationIcon(drawable);
        ((TextView)findViewById(R.id.tv_title)).setText("강의 추가하기");
    }

    private void getToken() {
        SharedPreferences pref = getSharedPreferences("store", 0);
        token = pref.getString("token", "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home || id == android.R.id.home){
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
