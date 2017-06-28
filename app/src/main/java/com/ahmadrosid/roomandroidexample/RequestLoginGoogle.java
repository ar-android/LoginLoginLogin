package com.ahmadrosid.roomandroidexample;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by ocittwo on 6/28/17.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */

public class RequestLoginGoogle {
    public String googleUserId;
    public String name;
    public String email;
    public String password;
    public String avatar;

    public static class Builder{

        private RequestLoginGoogle requestLoginGoogle;

        public Builder() {
            requestLoginGoogle = new RequestLoginGoogle();
        }

        public Builder setGoogleSignin(GoogleSignInResult googleSignInResult){
            GoogleSignInAccount account = googleSignInResult.getSignInAccount();
            requestLoginGoogle.googleUserId = account.getId();
            requestLoginGoogle.name = account.getDisplayName();
            requestLoginGoogle.email = account.getEmail();
            requestLoginGoogle.avatar = account.getPhotoUrl().toString();
            return this;
        }

        public Builder setPassword(String password){
            requestLoginGoogle.password = password;
            return this;
        }

        public Request build(){
            return new Request.Builder()
                    .post(RequestBody.create(MediaType.parse("application/json"), App.toJson(requestLoginGoogle)))
                    .url(App.getApiUrl())
                    .build();
        }
    }
}
