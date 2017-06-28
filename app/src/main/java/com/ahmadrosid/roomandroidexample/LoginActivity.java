package com.ahmadrosid.roomandroidexample;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.gson.Gson;
import com.hijamoya.rxlogin.RxLogin;

import java.util.Arrays;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LogAccess";

    private RxLogin mRxLogin = new RxLogin();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Message");
        progressDialog.setMessage("Loading...");

        findViewById(R.id.google).setOnClickListener(new View
                .OnClickListener() {
            @Override public void onClick(View v) {
                showLoading();
                Disposable subsGoogle = mRxLogin.loginGoogle(LoginActivity.this, new Scope(Scopes.PLUS_LOGIN))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<GoogleSignInResult>() {
                            @Override
                            public void accept(GoogleSignInResult googleSignInResult) throws Exception {
                                if (googleSignInResult.isSuccess()) {
                                    GoogleSignInAccount account = googleSignInResult.getSignInAccount();

                                    Disposable subscribe = ApiLogin.googleLogin(googleSignInResult, account.getId())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<ResponseSocialLogin>() {
                                                @Override public void accept(ResponseSocialLogin response) throws Exception {
                                                    Log.d(TAG, "accept: " + response);
                                                    hideLoading();
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override public void accept(Throwable throwable) throws Exception {
                                                    Log.d(TAG, "accept: " + App.toJson(throwable));
                                                    hideLoading();
                                                }
                                            });
                                    compositeDisposable.add(subscribe);

                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override public void accept(Throwable throwable) throws Exception {
                                // login fail
                                Log.d(TAG, "accept: " + throwable);
                            }
                        });
                compositeDisposable.add(subsGoogle);
            }
        });

        findViewById(R.id.facebook).setOnClickListener(new View
                .OnClickListener() {
            @Override public void onClick(View v) {
                showLoading();
                Disposable subsFacebook = mRxLogin.loginFacebook(LoginActivity.this, false, Arrays.asList(
                        "public_profile",
                        "email",
                        "user_friends"))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<LoginResult>() {
                            @Override public void accept(LoginResult loginResult) throws Exception {
                                Disposable subscribe = ApiLogin.facebookLogin(Profile.getCurrentProfile().getId(), "ocittwo@gmail.com")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ResponseSocialLogin>() {
                                            @Override public void accept(ResponseSocialLogin s) throws Exception {
                                                Log.d(TAG, "accept: " + s);
                                                hideLoading();
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override public void accept(Throwable throwable) throws Exception {
                                                Log.d(TAG, "accept: " + App.toJson(throwable));
                                            }
                                        });
                                compositeDisposable.add(subscribe);

                            }
                        }, new Consumer<Throwable>() {
                            @Override public void accept(Throwable throwable) throws Exception {
                                // login fail
                                Log.d(TAG, "throwable: " + toJson(throwable));
                            }
                        });
                compositeDisposable.add(subsFacebook);
            }
        });
    }


    private void showLoading() {
        progressDialog.show();
    }

    private void hideLoading() {
        progressDialog.dismiss();
    }

    private String toJson(Object o) {
        return new Gson().toJson(o);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRxLogin.onActivityResult(requestCode, resultCode, data);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        compositeDisposable.clear();
    }
}
