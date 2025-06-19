package com.example.session3.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groups")
data class Group (
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val name: String,
    val note : String,
)