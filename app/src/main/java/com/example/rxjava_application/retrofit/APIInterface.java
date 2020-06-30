package com.example.rxjava_application.retrofit;

import com.example.rxjava_application.pojo.Login;
import com.example.rxjava_application.pojo.PastPolicy;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("login")
    @FormUrlEncoded
    Observable<Login> LOGIN_OBSERVABLE(@Field("email") String email,
                                       @Field("password") String password);



    @POST("get_past_policy")
    @FormUrlEncoded
    Observable<PastPolicy> PAST_POLICY_OBSERVABLE(@Field("user_id") String userID);
}
