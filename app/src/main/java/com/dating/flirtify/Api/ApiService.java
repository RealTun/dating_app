package com.dating.flirtify.Api;

import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.Models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {
    // auth
    @POST("register")
    Call<User> register(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);

    @POST("logout")
    Call<User> logout(@Header("Authorization") String accessToken);

    @GET("user")
    Call<User> getUser(@Header("Authorization") String accessToken);

    // interest
//    @GET("interest-type")
//    Call<List<InterestType>> getInterestTypes(@Header("Authorization") String accessToken);

//    @GET("interest-type/user")
//    Call<List<UserInterest>> getInterestByUser(@Header("Authorization") String accessToken);

//    @POST("interest-type/user")
//    Call<Void> storeInterestUser(@Header("Authorization") String accessToken, @Body InterestType interestType);

//    @DELETE("interest-type/user/{id}")
//    Call<Void> deleteUserInterest(@Header("Authorization") String accessToken, @Path("id") int id);

    // Relationship routes
//    @GET("relationship-type")
//    Call<List<RelationshipType>> getRelationships(@Header("Authorization") String accessToken);

//    @GET("relationship-type/user")
//    Call<List<UserRelationship>> getRelationshipByUser(@Header("Authorization") String accessToken);

//    @POST("relationship-type/user")
//    Call<Void> storeUserRelationship(@Header("Authorization") String accessToken, @Body RelationshipType relationshipType);

//    @DELETE("relationship-type/user/{id}")
//    Call<Void> deleteUserRelationship(@Header("Authorization") String accessToken, @Path("id") int id);

    // Connect routes
    @GET("users-connect-no")
    Call<ArrayList<UserResponse>> getUserToConnect(@Header("Authorization") String accessToken);

    // Matcher routes
    @GET("matchers")
    Call<ArrayList<MatcherResponse>> getMatchers(@Header("Authorization") String accessToken);

//    @POST("matchers")
//    Call<Void> storeUserLike(@Header("Authorization") String accessToken, @Body LikeRequest likeRequest);

    // Photo routes
//    @GET("user-photos")
//    Call<List<Photo>> getUserPhotos(@Header("Authorization") String accessToken);
//
//    @POST("user-photos/upload")
//    Call<Void> storeUserPhotos(@Header("Authorization") String accessToken, @Body Photo photo);

    // Chat routes
}

