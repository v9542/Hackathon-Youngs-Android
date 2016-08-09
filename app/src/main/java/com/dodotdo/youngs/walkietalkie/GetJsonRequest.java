package com.dodotdo.youngs.walkietalkie;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KimYebon on 16. 8. 4..
 */
public class GetJsonRequest {
    static String token;

    public GetJsonRequest(Context context) {
        token = getToken(context, "", "");
    }

    public String getToken(Context context, String userId, String password) {
        SharedPreferences pref = context.getSharedPreferences("store", 0);

        return pref.getString("token", "");
    }

    public JSONObject getDefaultJSONObject(String namespace) {
        JSONObject originalObject = new JSONObject();

        try {
            originalObject.put("token", token);
            originalObject.put("namespace", namespace);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return originalObject;
    }


    public JSONObject getAuth() {
        JSONObject jsonObject = getDefaultJSONObject("auth");

        return jsonObject;
    }

    public JSONObject selectChannel(int channel_id) {
        JSONObject originalObject = getDefaultJSONObject("select_channel");
        JSONObject subObject = new JSONObject();

        try {
            subObject.put("channel_id", channel_id);

            originalObject.put("payload", subObject);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return originalObject;
    }

    public JSONObject occupyChannel(int channel_id) {
        JSONObject originalObject = getDefaultJSONObject("request_occupy_channel");
        JSONObject subObject = new JSONObject();

        try {
            subObject.put("channel_id", channel_id);

            originalObject.put("payload", subObject);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return originalObject;
    }

    public JSONObject releaseChannel(int channel_id) {
        JSONObject originalObject = getDefaultJSONObject("release_channel");
        JSONObject subObject = new JSONObject();

        try {
            subObject.put("channel_id", channel_id);

            originalObject.put("payload", subObject);
        } catch(JSONException e) {
            e.printStackTrace();
            return null;
        }

        return originalObject;
    }



}
