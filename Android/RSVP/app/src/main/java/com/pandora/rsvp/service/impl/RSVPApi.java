package com.pandora.rsvp.service.impl;

import com.pandora.rsvp.service.ApiCallBack;
import com.pandora.rsvp.service.IRSVPApi;
import com.pandora.rsvp.service.contract.AuthResponse;
import com.pandora.rsvp.service.contract.AuthenticationPayload;
import com.pandora.rsvp.service.contract.CreateOfferPayload;
import com.pandora.rsvp.service.contract.SingeUserInvitationResponse;
import com.pandora.rsvp.service.contract.UserInvitationsResponse;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

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

        @GET("api/users/{uid}/invitations/{invitation_id}/selectWinners")
        Call<SingeUserInvitationResponse> selectWinners(@Path("uid") String uid, @Path("invitation_id") String invitationId);

        @POST("api/users/{uid}/invitations")
        Call<SingeUserInvitationResponse> createOffer(@Path("uid") String uid, @Body CreateOfferPayload payload);



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
        executeApiCall(api.getInvitations("gmichalec"), invitationCallBack);
    }

    @Override
    public void selectWinners(String invitationId, ApiCallBack<SingeUserInvitationResponse> invitationsResponseApiCallBack) {
        executeApiCall(api.selectWinners("gmichalec", invitationId), invitationsResponseApiCallBack);
    }

    @Override
    public void createOffer(String title, int acceptLimit, long rsvpBy, String emailTo, String method, String body, ApiCallBack<SingeUserInvitationResponse> invitationResponseApiCallBack) {
        CreateOfferPayload payload = new CreateOfferPayload();
        payload.title = title;
        payload.response_accept_limit = acceptLimit;
        payload.rsvp_by_time = rsvpBy;
        payload.email_to = emailTo;
        payload.method = method;
        payload.invitation_body = body;
        executeApiCall(api.createOffer("gmichalec", payload), invitationResponseApiCallBack);
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
