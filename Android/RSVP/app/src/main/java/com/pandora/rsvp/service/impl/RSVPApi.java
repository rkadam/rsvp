package com.pandora.rsvp.service.impl;

import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.AuthResponse;
import com.pandora.rsvp.service.contract.AuthenticationPayload;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class RSVPApi implements IRSVPApi {

    private final RetrofitRSVPApi api;
    private static final String API_ENDPOINT = "http://aai.savagebeast.com:9000";

    public RSVPApi() {
        OkHttpClient client =new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_ENDPOINT)
                .client(client) // For full logging.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(RetrofitRSVPApi.class);
    }

    private interface RetrofitRSVPApi {

        @POST("api/login")
        Call<AuthResponse> authenticate(@Body AuthenticationPayload payload);

        @GET("api/users/{uid}/invitations")
        Call<UserInvitationsResponse> getInvitations(@Path("uid") String uid);

    }

    @Override
    public void authenticate(String email, String password, ApiCallBack<AuthResponse> authCallback) {
        AuthenticationPayload payload = new AuthenticationPayload();
        payload.uid = email;
        payload.password = password;
        executeApiCall(api.authenticate(payload), authCallback);
    }

    @Override
    public void logout(ApiCallBack<AuthResponse> authCallBack) {

    }

    @Override
    public void getInvitations(ApiCallBack<UserInvitationsResponse> invitationCallBack) {
        executeApiCall(api.getInvitations("fakeId"), invitationCallBack);
    }

    private <T> void executeApiCall(Call<T> retrofitCall, final ApiCallBack<T> clientCallBack) {
        retrofitCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Response<T> response, Retrofit retrofit) {
                if (clientCallBack != null) {
                    clientCallBack.onSuccess(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                if (clientCallBack != null) {
                    clientCallBack.onFailure(t);
                }
            }
        });
    }
}
