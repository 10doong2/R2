package com.doongis.r2.drawing;

import android.app.Application;

import com.firebase.client.Firebase;

public class DrawingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        //Firebase.getDefaultConfig().setLogLevel(Logger.Level.DEBUG);
        SyncedBoardManager.setContext(this);
    }
}
