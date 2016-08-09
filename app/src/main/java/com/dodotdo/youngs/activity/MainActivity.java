package com.dodotdo.youngs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dodotdo.youngs.R;
import com.dodotdo.youngs.server.NetDefine;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    String token;
    int id;

    Handler mHandler;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        mHandler = new Handler();
        getTokenAndId();
        setWebView();
    }

    private void setWebView() {

        webView = (WebView) findViewById(R.id.webview);
        webView.addJavascriptInterface(new AndroidBridge(), "youngs");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(NetDefine.getWebPath() + "/#home?token=" + token + "&member_id=" + id);
    }

    private void getTokenAndId() {
        SharedPreferences pref = getSharedPreferences("store", 0);
        token = pref.getString("token", "");
        id = pref.getInt("id", 1);
    }

    private class AndroidBridge {

        @JavascriptInterface
        public void setMessage(final String arg) {
            mHandler.post(new Runnable() {
                public void run() {
                    int tag = Integer.parseInt(arg);
                    switch(tag) {
                        case -1:
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                            break;
                        case -2:
                            startActivity(new Intent(MainActivity.this, MakeChannelActivity.class));
                            break;
                        default:
                            startActivity(new Intent(MainActivity.this, WalkieTalkieActivity.class).putExtra("channel_id", tag));
                            break;

                    }
                }
            });
        }
    }

    protected void makeEndDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("정말 종료하시겠습니까?")
                .setTitle("Youngs")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }
                )
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        MainActivity.activity.finish();
                    }
                });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        makeEndDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
