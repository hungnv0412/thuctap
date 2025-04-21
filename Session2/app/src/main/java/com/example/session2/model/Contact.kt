package com.example.session2.model

data class Contact (
    val id: Int,
    val name: String,
    val phoneNumber: String,
    val avatar:Int,
    val email: String = "nvh@gmail.com",
    val note: String = "note here",
)

