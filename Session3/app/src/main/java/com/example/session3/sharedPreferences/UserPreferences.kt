package com.example.session3.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserPreferences {
    private const val PREFS_NAME = "user_prefs"
    private const val KEY_USERNAME = "username"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setUsername(username: String) {
        sharedPreferences.edit { putString(KEY_USERNAME, username) }
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, "")
    }

}