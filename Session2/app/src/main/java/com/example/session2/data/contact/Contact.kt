package com.example.session2.data.contact

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact (

    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val phoneNumber: String,
    val avatar:Int,
    val email: String = "nvh@gmail.com",
    val note: String = "note here",
)

data class ContactUpdatePhoneNumber(
    val phoneNumber: String,
)