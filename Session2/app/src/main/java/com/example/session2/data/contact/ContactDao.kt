package com.example.session2.data.contact

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertContact(contact: Contact)
    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>
    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?
    @Query("DELETE FROM contacts WHERE id = :id")
    suspend fun deleteContactById(id: Int) : Int
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(contacts: List<Contact>)
    @Query("SELECT * FROM contacts INNER JOIN contacts_groups_cross_ref ON contacts.id = contacts_groups_cross_ref.contactId WHERE contacts_groups_cross_ref.groupId = :groupId")
    fun getContactsByGroupId(groupId: Int): List<Contact>
}