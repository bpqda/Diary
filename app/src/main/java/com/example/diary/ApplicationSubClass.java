package com.example.diary;

import android.app.Application;

import io.realm.Realm;

public class ApplicationSubClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
