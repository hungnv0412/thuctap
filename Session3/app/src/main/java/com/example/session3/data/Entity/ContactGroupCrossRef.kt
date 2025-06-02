package com.example.session3.data.Entity

import androidx.room.Entity

@Entity(tableName = "contacts_groups_cross_ref", primaryKeys = ["contactId","groupId"])
data class ContactGroupCrossRef(
    val contactId : Int,
    val groupId : Int,
)