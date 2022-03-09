package com.example.project_yougo.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Model {
    public static Model instance;
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());


    /**
     * Authentication
     */
//TODO: fix "isSignedIn()"
    public boolean isSignedIn() {
        return false;
        //return FirebaseDatabaseHandler.isSignedIn();
    }
}
