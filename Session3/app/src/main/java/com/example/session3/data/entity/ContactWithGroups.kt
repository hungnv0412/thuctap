package com.example.session3.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ContactWithGroups(
    @Embedded val contact: Contact,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ContactGroupCrossRef::class,
            parentColumn = "contactId",
            entityColumn = "groupId"
        )
    )
    val groups: List<Group>
)