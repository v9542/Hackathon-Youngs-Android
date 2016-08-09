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
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by kimyebon on 2016. 8. 6..
 */
public interface ServerAPI {

    @POST("/api/member")
    Call<BasicResponse> signUp(@Body SignUpRequest signUpRequest);

    @POST("/api/auth/member")
    Call<LoginResponse> goLogin(@Body LoginRequest registerAccount);

    @GET("/api/lecture/{lecture_id}")
    Call<LectureResponse> getLectureInfo(@Path("lecture_id")int channel_id, @Query("level")String level);

    @POST("/api/lecture/{lecture_id}/attend")
    Call<BasicResponse> attendLecture(@Path("lecture_id")int channel_id);

    @DELETE("/api/lecture/{lecture_id}/attend")
    Call<BasicResponse> outLecture(@Path("lecture_id")int channel_id);

    @GET("/api/question")
    Call<QuestionResponse> getQuestion(@Query("type")String type);

    @GET("/api/voice/{lecture_id}")
    Call<HistoryResponse> getHistory(@Path("lecture_id")int channel_id);

    @POST("/api/lecture/{lecture_id}/occupy")
    Call<BasicResponse> occupyChannel(@Path("lecture_id")int channel_id);

    @DELETE("/api/lecture/{lecture_id}/occupy")
    Call<BasicResponse> releaseChannel(@Path("lecture_id")int channel_id);

    @GET("/api/lecture/{lecture_id}/listener")
    Call<AccountResponse> getListener(@Path("lecture_id")int channel_id);

    @POST("/api/lecture")
    Call<BasicResponse> makeChannel(@Body ChannelRequest request);



}
