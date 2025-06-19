package com.example.session2.data.contactGroup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.session2.data.contactGroup.ContactWithGroupRef

@Dao
interface ContactGroupDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addContactToGroup(contactGroupCrossRef: ContactWithGroupRef)
    @Query("DELETE FROM contacts_groups_cross_ref WHERE contactId=:contactId AND groupId=:groupId ")
    suspend fun deleteContactFromGroup(contactId: Int,groupId: Int)
}