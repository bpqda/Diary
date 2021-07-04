package com.example.diary;

import io.realm.RealmConfiguration;

public class RealmUtility {
    private static final int SCHEMA_NOW = 2;

    public static RealmConfiguration getDefaultConfig() {
        return new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .schemaVersion(SCHEMA_NOW)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}