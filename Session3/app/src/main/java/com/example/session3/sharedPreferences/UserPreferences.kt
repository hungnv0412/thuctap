package com.example.session3.sharedPreferences
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPreferences @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences = context.getSharedPreferences(KEY_USER_NAME, Context.MODE_PRIVATE)
    fun saveUserName(name: String) {
        sharedPreferences.edit().putString(KEY_USER_NAME, name).apply()
    }
    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_USER_NAME, "")?:""
    }
    companion object {
        private const val KEY_USER_NAME = "user_name"
    }
}