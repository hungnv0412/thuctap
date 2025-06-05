package com.example.session3.data.Entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class GroupWithContacts (
    @Embedded val group: Group,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ContactGroupCrossRef::class,
            parentColumn = "groupId",
            entityColumn = "contactId"
        )
    )
    val contacts: List<Contact>
)