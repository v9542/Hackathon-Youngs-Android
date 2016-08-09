package com.dodotdo.youngs.server;

import com.dodotdo.youngs.server.request.ChannelRequest;
import com.dodotdo.youngs.server.request.LoginRequest;
import com.dodotdo.youngs.server.request.SignUpRequest;
import com.dodotdo.youngs.server.response.AccountResponse;
import com.dodotdo.youngs.server.response.BasicResponse;
import com.dodotdo.youngs.server.response.HistoryResponse;
import com.dodotdo.youngs.server.response.LectureResponse;
import com.dodotdo.youngs.server.response.LoginResponse;
import com.dodotdo.youngs.server.response.QuestionResponse;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Omjoon on 2015. 12. 7..
 */
public class ServerQuery {
    public static void goLogin(LoginRequest request, retrofit.Callback callback){
        Call<LoginResponse> call = ServiceGenerator.createService(ServerAPI.class).goLogin(request);
        call.enqueue(callback);
    }

    public static void signUp(SignUpRequest request, retrofit.Callback callback){
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class).signUp(request);
        call.enqueue(callback);
    }

    public static void getLectureInfo(int channel_id, String token, retrofit.Callback callback) {
        Call<LectureResponse> call = ServiceGenerator.createService(ServerAPI.class, token).getLectureInfo(channel_id, "listener");
        call.enqueue(callback);
    }

    public static void attendLecture(int channel_id, String token, retrofit.Callback callback) {
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class, token).attendLecture(channel_id);
        call.enqueue(callback);
    }

    public static void outLecture(int channel_id, String token, retrofit.Callback callback) {
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class, token).outLecture(channel_id);
        call.enqueue(callback);
    }

    public static void getQuestion(String type, String token, retrofit.Callback callback) {
        Call<QuestionResponse> call = ServiceGenerator.createService(ServerAPI.class, token).getQuestion(type);
        call.enqueue(callback);
    }

    public static void getHistory(int channel_id, String token, retrofit.Callback callback) {
        Call<HistoryResponse> call = ServiceGenerator.createService(ServerAPI.class, token).getHistory(channel_id);
        call.enqueue(callback);
    }

    public static void occupyChannel(int channel_id, String token, retrofit.Callback callback) {
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class, token).occupyChannel(channel_id);
        call.enqueue(callback);
    }

    public static void releaseChannel(int channel_id, String token, retrofit.Callback callback) {
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class, token).releaseChannel(channel_id);
        call.enqueue(callback);
    }

    public static void getListener(int channel_id, String token, retrofit.Callback callback) {
        Call<AccountResponse> call = ServiceGenerator.createService(ServerAPI.class, token).getListener(channel_id);
        call.enqueue(callback);
    }

    public static void makeChannel(ChannelRequest request, String token, retrofit.Callback callback) {
        Call<BasicResponse> call = ServiceGenerator.createService(ServerAPI.class, token).makeChannel(request);
        call.enqueue(callback);
    }



}
