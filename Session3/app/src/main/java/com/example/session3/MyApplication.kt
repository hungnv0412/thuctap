package com.example.session3

import android.app.Application
import com.example.session3.sharedPreferences.UserPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        // Initialize the database or any other components if needed
        UserPreferences.init(this)
    }
}