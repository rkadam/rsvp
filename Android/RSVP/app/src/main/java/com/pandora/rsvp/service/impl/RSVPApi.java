package com.pandora.rsvp.service.impl;

import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;


import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.POST;

/**
 * Copyright (c) 2015 Pandora 2015, Inc
 */
public class RSVPApi implements IRSVPApi {

    private final RetrofitRSVPApi api;

    public RSVPApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("tbd")//Todo: to be determined
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(RetrofitRSVPApi.class);
    }

    interface RetrofitRSVPApi {
        @POST
        Call<Boolean> authenticate(String email, String password);

    }

    @Override
    public void authenticate(String email, String password, ApiCallBack<Boolean> authCallback) {
        executeApiCall(api.authenticate(email, password), authCallback);
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
