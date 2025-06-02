package com.example.session3.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.session3.data.Entity.Contact
import com.example.session3.data.Entity.ContactWithGroups

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact> // Removed default return value

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Delete
    suspend fun deleteContact(contact: Contact)
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(contacts: List<Contact>)
    @Transaction
    @Query("SELECT * FROM contacts WHERE id= :contactId")
    suspend fun getContactWithGroup(contactId: Int): ContactWithGroups
}