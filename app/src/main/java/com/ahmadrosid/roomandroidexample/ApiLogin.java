package com.ahmadrosid.roomandroidexample;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.gson.Gson;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;

/**
 * Created by ocittwo on 6/28/17.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */

public class ApiLogin {

    private static OkHttpClient.Builder client = new OkHttpClient.Builder()
            .addInterceptor(new LoggingInterceptor.Builder()
                    .loggable(BuildConfig.DEBUG)
                    .setLevel(Level.BASIC)
                    .log(Platform.INFO)
                    .request("Request")
                    .response("Response")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .build());
    private static OkHttpClient okHttpClient = client.build();

    public static Observable<ResponseSocialLogin> googleLogin(final GoogleSignInResult googleSignInResult, final String password) {
        return Observable.create(new ObservableOnSubscribe<ResponseSocialLogin>() {
            @Override public void subscribe(final ObservableEmitter<ResponseSocialLogin> subs) throws Exception {
                okHttpClient.newCall(new RequestLoginGoogle.Builder()
                        .setGoogleSignin(googleSignInResult)
                        .setPassword(password)
                        .build()
                ).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        subs.onError(e);
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        ResponseSocialLogin responseSocialLogin = new Gson().fromJson(json, ResponseSocialLogin.class);
                        subs.onNext(responseSocialLogin);
                        subs.onComplete();
                    }
                });
            }
        });
    }

    public static Observable<ResponseSocialLogin> facebookLogin(final String password, final String email) {
        return Observable.create(new ObservableOnSubscribe<ResponseSocialLogin>() {
            @Override public void subscribe(final ObservableEmitter<ResponseSocialLogin> subs) throws Exception {
                okHttpClient.newCall(new RequestFacebookLogin.Builder()
                        .setEmail(email)
                        .setPassword(password)
                        .build()
                ).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        subs.onError(e);
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        ResponseSocialLogin responseSocialLogin = new Gson().fromJson(json, ResponseSocialLogin.class);
                        subs.onNext(responseSocialLogin);
                        subs.onComplete();
                    }
                });
            }
        });
    }


    public static Observable<ResponseSpinnerData> getSpinnerData() {
        return Observable.create(new ObservableOnSubscribe<ResponseSpinnerData>() {
            @Override public void subscribe(final ObservableEmitter<ResponseSpinnerData> subs) throws Exception {
                String token = "v5p9wfljp81TGxgiH71PGAjJpRDb4nbCF3cmkCUSt1Pxsidu0FEbaO6pmnV5";
                Request request = new Request.Builder()
                        .url("http://139.59.124.59:4400/api/v1/level?token=" + token)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        subs.onError(e);
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        String json = response.body().string();
                        ResponseSpinnerData responseSpinnerData = new Gson().fromJson(json, ResponseSpinnerData.class);
                        subs.onNext(responseSpinnerData);
                        subs.onComplete();
                    }
                });
            }
        });
    }

}
