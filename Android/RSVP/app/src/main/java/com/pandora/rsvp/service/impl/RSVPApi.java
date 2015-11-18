package com.pandora.rsvp.service.impl;

import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.AuthenticationPayload;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class RSVPApi implements IRSVPApi {

    private final RetrofitRSVPApi api;

    public RSVPApi() {
        OkHttpClient client =new OkHttpClient();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(interceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://someapi.com")//Todo: replace by domain
                .client(client) // For full logging.
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(RetrofitRSVPApi.class);
    }

    private interface RetrofitRSVPApi {

        @POST("someApiToAuthenticate")
        Call<Boolean> authenticate(@Body AuthenticationPayload payload);

    }

    @Override
    public void authenticate(String email, String password, ApiCallBack<Boolean> authCallback) {
        AuthenticationPayload payload = new AuthenticationPayload();
        payload.email = email;
        payload.password = password;
        executeApiCall(api.authenticate(payload), authCallback);
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
