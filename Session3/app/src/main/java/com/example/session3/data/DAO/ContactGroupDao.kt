package com.example.session3.data.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.session3.data.Entity.ContactGroupCrossRef

@Dao
interface ContactGroupDao{
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun addContactToGroup(contactGroupCrossRef: ContactGroupCrossRef)
    @Query("DELETE FROM contacts_groups_cross_ref WHERE contactId=:contactId AND groupId=:groupId ")
    suspend fun deleteContactFromGroup(contactId: Int,groupId: Int)
}