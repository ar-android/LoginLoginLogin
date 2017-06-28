package com.ahmadrosid.roomandroidexample;

import com.facebook.Profile;

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

public class RequestFacebookLogin {
    public String facebookUserId;
    public String name;
    public String password;
    public String email;

    public static class Builder{
        private RequestFacebookLogin requestFacebookLogin;
        public Builder() {
            requestFacebookLogin = new RequestFacebookLogin();
        }

        public Builder setEmail(String email){
            requestFacebookLogin.email = email;
            return this;
        }

        public Builder setPassword(String password){
            requestFacebookLogin.password = password;
            return this;
        }

        public Request build(){
            Profile profile = Profile.getCurrentProfile();
            requestFacebookLogin.facebookUserId = profile.getId();
            requestFacebookLogin.name = profile.getName();
            return new Request.Builder()
                    .url(App.getApiUrl())
                    .post(RequestBody.create(MediaType.parse("application/json"), App.toJson(requestFacebookLogin)))
                    .build();
        }
    }
}
