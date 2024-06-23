package com.dating.flirtify.Api;

import com.dating.flirtify.Models.InterestType;
import com.dating.flirtify.Models.Requests.InterestRequest;
import com.dating.flirtify.Models.Requests.LikeRequest;
import com.dating.flirtify.Models.Requests.MessageRequest;
import com.dating.flirtify.Models.Requests.LoginRequest;
import com.dating.flirtify.Models.Requests.RegisterRequest;
import com.dating.flirtify.Models.Requests.RelationshipRequest;
import com.dating.flirtify.Models.Responses.InterestResponse;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.Models.Responses.MessageResponse;
import com.dating.flirtify.Models.Responses.RelationshipResponse;
import com.dating.flirtify.Models.Responses.UserResponse;
import com.dating.flirtify.Models.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // auth
    @POST("register")
    Call<LoginResponse> register(@Body RegisterRequest registerRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String accessToken);

    @GET("user")
    Call<UserResponse> getUser(@Header("Authorization") String accessToken);

    // interest
    @GET("interest-type")
    Call<List<InterestType>> getInterestTypes(@Header("Authorization") String accessToken);

    @GET("interest-type/user")
    Call<InterestResponse> getInterestByUser(@Header("Authorization") String accessToken);

    @POST("interest-type/user")
    Call<Void> storeInterestUser(@Header("Authorization") String accessToken, @Body InterestRequest interestRequest);

//    @DELETE("interest-type/user/{id}")
//    Call<Void> deleteUserInterest(@Header("Authorization") String accessToken, @Path("id") int id);

    // Relationship routes
    @GET("relationship-type")
    Call<ArrayList<RelationshipResponse>> getRelationships();

    @PATCH("relationship-type/user")
    Call<Void> updateRelationshipType(@Header("Authorization") String accessToken, @Body RelationshipRequest relationshipRequest);

//    @GET("relationship-type/user")
//    Call<List<UserRelationship>> getRelationshipByUser(@Header("Authorization") String accessToken);

//    @POST("relationship-type/user")
//    Call<Void> storeUserRelationship(@Header("Authorization") String accessToken, @Body RelationshipType relationshipType);

//    @DELETE("relationship-type/user/{id}")
//    Call<Void> deleteUserRelationship(@Header("Authorization") String accessToken, @Path("id") int id);

    // Connect routes
    @GET("users-connect")
    Call<ArrayList<UserResponse>> getUserToConnect(@Header("Authorization") String accessToken);

    // Matcher routes
    @GET("matchers")
    Call<ArrayList<MatcherResponse>> getMatchers(@Header("Authorization") String accessToken);

    @POST("matchers")
    Call<Void> storeUserLike(@Header("Authorization") String accessToken, @Body LikeRequest likeRequest);

    // Photo routes
//    @GET("user-photos")
//    Call<List<Photo>> getUserPhotos(@Header("Authorization") String accessToken);
//
//    @POST("user-photos/upload")
//    Call<Void> storeUserPhotos(@Header("Authorization") String accessToken, @Body Photo photo);

    // Chat routes
//    @GET("/chat")
//    Call<List<Message>> getChats();

    @GET("chat/messages/{receiver_id}")
    Call<ArrayList<MessageResponse>> getMessages(@Header("Authorization") String accessToken, @Path("receiver_id") int receiverId);

    @POST("chat/send")
    Call<MessageResponse> sendMessage(@Header("Authorization") String accessToken, @Body MessageRequest messageRequest);
}

