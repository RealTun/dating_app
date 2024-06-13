package com.dating.flirtify.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "login_pref"; // Tên file SharedPreferences
    private static final String PREF_ACCESS_TOKEN = "access_token";
    private static final String PREF_TOKEN_TYPE = "token_type";
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private Gson gson;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public void saveLoginSession(String accessToken, String tokenType) {
        editor.putString(PREF_ACCESS_TOKEN, accessToken);
        editor.putString(PREF_TOKEN_TYPE, tokenType);
        editor.apply();
    }

    public static String getAccessToken() {
        return sharedPreferences.getString(PREF_ACCESS_TOKEN, null);
    }

    public static String getTokenType() {
        return sharedPreferences.getString(PREF_TOKEN_TYPE, null);
    }

    public static String getToken() {
        return getTokenType() + " " + getAccessToken();
    }

    public static void clearSession() {
        editor.remove(PREF_ACCESS_TOKEN);
        editor.remove(PREF_TOKEN_TYPE);
        editor.apply();
    }
}
