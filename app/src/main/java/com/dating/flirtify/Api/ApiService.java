package com.dating.flirtify.Api;

import com.dating.flirtify.Models.InterestType;
import com.dating.flirtify.Models.Requests.ChangePasswordRequest;
import com.dating.flirtify.Models.Requests.CheckEmailRequest;
import com.dating.flirtify.Models.Requests.InterestRequest;
import com.dating.flirtify.Models.Requests.LikeRequest;
import com.dating.flirtify.Models.Requests.MessageRequest;
import com.dating.flirtify.Models.Requests.LoginRequest;
import com.dating.flirtify.Models.Requests.PhotoRequest;
import com.dating.flirtify.Models.Requests.PreferenceRequest;
import com.dating.flirtify.Models.Requests.RegisterRequest;
import com.dating.flirtify.Models.Requests.RelationshipRequest;
import com.dating.flirtify.Models.Requests.UserLocationRequest;
import com.dating.flirtify.Models.Requests.UserRequest;
import com.dating.flirtify.Models.Responses.InterestResponse;
import com.dating.flirtify.Models.Responses.LoginResponse;
import com.dating.flirtify.Models.Responses.MatcherResponse;
import com.dating.flirtify.Models.Responses.MessageResponse;
import com.dating.flirtify.Models.Responses.RelationshipResponse;
import com.dating.flirtify.Models.Responses.UserResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // auth
    @POST("checkDuplicateEmail")
    Call<Void> checkDuplicateEmail(@Body CheckEmailRequest checkEmailRequest);

    @POST("register")
    Call<LoginResponse> register(@Body RegisterRequest registerRequest);

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String accessToken);

    @PATCH("changePassword")
    Call<Void> updatePassword(@Body ChangePasswordRequest changePasswordRequest);

    @GET("user")
    Call<UserResponse> getUser(@Header("Authorization") String accessToken);

    @PATCH("user")
    Call<Void> updateUser(@Header("Authorization") String accessToken, @Body UserRequest user);

    @PATCH("user/location")
    Call<Void> updateUserLocation(@Header("Authorization") String accessToken, @Body UserLocationRequest userLocationRequest);

    @PATCH("user/updatePreference")
    Call<Void> updatePreference(@Header("Authorization") String accessToken, @Body PreferenceRequest userLocationRequest);

    @POST("user-photos/upload")
    Call<Void> storeUserPhotos(@Header("Authorization") String accessToken, @Body PhotoRequest photoRequest);

    @POST("user/photos/delete")
    Call<Void> deleteUserPhotos(@Header("Authorization") String accessToken, @Body RequestBody photoRequest);

    @DELETE("interest-type/user/{id}")
    Call<Void> deleteUserInterest(@Header("Authorization") String accessToken, @Path("id") int id);

    // interest
    @GET("interest-type")
    Call<List<InterestType>> getInterestTypes(@Header("Authorization") String accessToken);

    @GET("interest-type/user")
    Call<InterestResponse> getInterestByUser(@Header("Authorization") String accessToken);

    @POST("interest-type/user")
    Call<Void> storeInterestUser(@Header("Authorization") String accessToken, @Body InterestRequest interestRequest);


    // Relationship routes
    @GET("relationship-type")
    Call<ArrayList<RelationshipResponse>> getRelationships();

    @PATCH("relationship-type/user")
    Call<Void> updateRelationshipType(@Header("Authorization") String accessToken, @Body RelationshipRequest relationshipRequest);

    // Connect routes
    @GET("users-connect")
    Call<ArrayList<UserResponse>> getUserToConnect(@Header("Authorization") String accessToken);

    // Matcher routes
    @GET("matchers")
    Call<ArrayList<MatcherResponse>> getMatchers(@Header("Authorization") String accessToken);

    @POST("matchers")
    Call<Void> storeUserLike(@Header("Authorization") String accessToken, @Body LikeRequest likeRequest);

    @GET("chat/messages/{receiver_id}")
    Call<ArrayList<MessageResponse>> getMessages(@Header("Authorization") String accessToken, @Path("receiver_id") int receiverId);

    @POST("chat/send")
    Call<MessageResponse> sendMessage(@Header("Authorization") String accessToken, @Body MessageRequest messageRequest);
}

