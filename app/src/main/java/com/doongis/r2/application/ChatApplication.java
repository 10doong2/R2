package com.doongis.r2.application;

import android.app.Application;

import com.firebase.client.Firebase;

public class ChatApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
