package com.example.groupprojectict602;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class InventoryApp extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        FirebaseApp.initializeApp(this);
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            FirebaseApp.initializeApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
