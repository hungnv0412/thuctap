package com.example.session2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertContact(contact: Contact)
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?
    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Int)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(contacts: List<Contact>)
    @Query("DELETE FROM contacts")
    fun deleteAllContacts()
}