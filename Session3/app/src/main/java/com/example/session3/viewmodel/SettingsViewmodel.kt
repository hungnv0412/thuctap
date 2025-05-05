package com.example.session3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.session3.sharedPreferences.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username
    init {
        _username.value = userPreferences.getUserName()
    }
    fun saveUserName(name: String) {
        userPreferences.saveUserName(name)
        _username.value = name
    }

}