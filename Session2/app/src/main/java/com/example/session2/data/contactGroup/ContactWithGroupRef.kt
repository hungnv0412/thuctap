package com.example.session2.data.contactGroup

import androidx.room.Entity

@Entity(tableName = "contacts_groups_cross_ref", primaryKeys = ["contactId", "groupId"])
data class ContactWithGroupRef(
    val contactId: Int,
    val groupId: Int,
)