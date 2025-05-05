package com.example.session3.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Insert
    suspend fun insertContact(contact: Contact)
    
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact> // Removed default return value
    
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?
    
    @Delete
    suspend fun deleteContact(contact: Contact)
}
