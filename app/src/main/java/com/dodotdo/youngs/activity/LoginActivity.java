package com.dodotdo.youngs.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dodotdo.youngs.R;
import com.dodotdo.youngs.Util.Util;
import com.dodotdo.youngs.data.Login;
import com.dodotdo.youngs.server.ServerQuery;
import com.dodotdo.youngs.server.request.LoginRequest;
import com.dodotdo.youngs.server.response.LoginResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LoginActivity extends AppCompatActivity {

    Button loginView;
    TextView signUpView;
    EditText emailView, passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize() {
        loginView = (Button) findViewById(R.id.signin_btn);
        emailView = (EditText) findViewById(R.id.email_edit_signin);
        passwordView = (EditText)findViewById(R.id.password_edit_signin);
        signUpView = (TextView)findViewById(R.id.signup_btn_signin);

        loginView.setOnClickListener(clickListener);
        signUpView.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signin_btn:
                    login(collectData());
                    break;
                case R.id.signup_btn_signin:
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                    break;
            }
        }
    };

    protected LoginRequest collectData() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(emailView.getText().toString());
        loginRequest.setPassword(passwordView.getText().toString());

        return loginRequest;
    }

    public void login(LoginRequest loginRequest) {
        ServerQuery.goLogin(loginRequest, new Callback() {

            @Override
            public void onResponse(Response response, Retrofit retrofit) {
                String message = response.message();
                if(message.equals("OK")) {
                    LoginResponse loginResponse = (LoginResponse) response.body();
                    storeTokenAndId(loginResponse.getResult());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }else if(message.equals("UNAUTHORIZED")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if(message.equals("NOT FOUND")) {
                    Toast.makeText(getApplicationContext(), "이메일을 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "오류!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {

                Log.d("Yongs", t.getMessage() + "[Login]");
            }
        });
    }

    public void storeTokenAndId(Login data) {
        SharedPreferences pref = getSharedPreferences("store", 0);
        SharedPreferences.Editor editor = pref.edit();

        String token = data.getToken();
        int id = data.getId();

        editor.putString("token", token);
        editor.putInt("id", id);
        editor.commit();
    }
}
