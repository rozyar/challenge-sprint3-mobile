package com.example.aicreditanalyzer.activity

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        val firebaseApp = FirebaseApp.getInstance()
        if (firebaseApp != null) {
            Log.d("MyApplication", "Firebase initialized successfully")
        } else {
            Log.e("MyApplication", "Firebase initialization failed")
        }
    }
}
