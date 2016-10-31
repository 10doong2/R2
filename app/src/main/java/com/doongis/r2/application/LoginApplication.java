package com.doongis.r2.application;

import android.app.Application;
import com.facebook.FacebookSdk;
import com.firebase.client.Firebase;

public class LoginApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(this);
    }
}