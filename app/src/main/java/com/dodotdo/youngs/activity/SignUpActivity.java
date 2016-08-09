package com.dodotdo.youngs.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.dodotdo.youngs.R;
import com.dodotdo.youngs.Util.Util;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.request.LoginRequest;
import com.dodotdo.youngs.server.request.SignUpRequest;
import com.dodotdo.youngs.server.response.LoginResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    ImageView profileView;
    EditText emailEdit, nicknameEdit, passwordEdit, rePasswordEdit;
    Button signUpBtn;

    final int photoCode = 9999;

    String path;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeActionBar();
        initialize();

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

    private void initialize() {
        profileView = (ImageView)findViewById(R.id.profileImage);
        emailEdit = (EditText)findViewById(R.id.email_edit_signup);
        nicknameEdit = (EditText)findViewById(R.id.nickname_edit_signup);
        passwordEdit = (EditText)findViewById(R.id.password_edit_signup);
        rePasswordEdit = (EditText)findViewById(R.id.repassword_edit_signup);
        signUpBtn = (Button)findViewById(R.id.signup_btn_signup);


        signUpBtn.setOnClickListener(mListener);
        profileView.setOnClickListener(mListener);

        profileView.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_default));
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.signup_btn_signup :
                    signUp();
                    break;
                case R.id.profileImage:
                    Intent cameraIntent = new Intent(Intent.ACTION_PICK);
                    cameraIntent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    cameraIntent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(cameraIntent, photoCode);
                    break;
            }
        }
    };

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



    public void signUp() {
        String email = emailEdit.getText().toString();
        String pw = passwordEdit.getText().toString();
        String rePw = rePasswordEdit.getText().toString();
        String nickname = nicknameEdit.getText().toString();
        String image = imageToString();
        SignUpRequest request;

        if(email.equals("") || email.equals(" ")) {
            Util.makeDialogAndShow("회원가입 오류", "이메일을 입력해주세요.", SignUpActivity.this);
            return;
        }
        if(pw.equals("") || pw.equals(" ")) {
            Util.makeDialogAndShow("회원가입 오류", "비밀번호를 입력해주세요.", SignUpActivity.this);
            return;
        }
        if(nickname.equals("") || nickname.equals(" ")) {
            Util.makeDialogAndShow("회원가입 오류", "닉네임을 입력해주세요.", SignUpActivity.this);
            return;
        }

        if(!pw.equals(rePw)) {
            Util.makeDialogAndShow("회원가입 오류", "비밀번호가 일치하지 않습니다.", SignUpActivity.this);
            return;
        }
        if(!(nickname.length()>=2&&nickname.length()<=10)) {
            Util.makeDialogAndShow("회원가입 오류", "닉네임을 2~10자 사이로 입력하세요.", SignUpActivity.this);
            return;
        }
        if(pw.length()<=4) {
            Util.makeDialogAndShow("회원가입 오류", "비밀번호를 5자 이상으로 입력하세요.", SignUpActivity.this);
            return;
        }
        request = new SignUpRequest(email, pw, nickname, image);

        serverCommunication(request);
    }


    public void afterSuccessServerCommunication() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        finish();
    }

    public void serverCommunication(SignUpRequest signUpRequest) {
        ServerQuery.signUp(signUpRequest, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if(message.equals("OK")) {
                    afterSuccessServerCommunication();
                }else if(message.equals("NOT ACCEPTABLE")){
                    Util.makeDialogAndShow("회원가입 오류", "양식을 지키고 사진을 등록해주세요.", SignUpActivity.this);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                //TODO 중복 확인
                Log.d("Yongs", t.getMessage() + "[Login]");
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
                    profileView.setBackgroundDrawable(image);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home || id == android.R.id.home){
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


}
