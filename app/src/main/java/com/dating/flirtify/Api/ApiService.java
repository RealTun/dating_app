package com.dating.flirtify.Api;

import com.dating.flirtify.Models.Matcher;
import com.dating.flirtify.Models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    @POST("register")
    Call<User> register(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);

    @POST("logout")
    Call<User> logout(@Header("Authorization") String accessToken);

    @GET("user")
    Call<User> getUser(@Header("Authorization") String accessToken);

    @GET("matchers")
    Call<Matcher> getMatchers(@Header("Authorization") String accessToken);
}

