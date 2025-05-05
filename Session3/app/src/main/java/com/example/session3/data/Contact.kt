package com.example.session3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val avatar:Int,
    val email: String = "nvh@gmail.com",
    val note: String = "note here",
)

