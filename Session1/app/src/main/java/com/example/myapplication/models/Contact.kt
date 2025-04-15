package com.example.myapplication.models

data class Contact (
    val name: String,
    val phoneNumber: String,
    val avatar:Int,
    val email: String = "nvh@gmail.com",
    val note: String = "note here",
)

