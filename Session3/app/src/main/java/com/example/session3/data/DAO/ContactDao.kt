package com.example.session3.data.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.session3.data.entity.Contact

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertContact(contact: Contact)

    @Query("SELECT * FROM contacts")
    suspend fun getAllContacts(): List<Contact>

    @Query("SELECT * FROM contacts WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?
    @Update
    suspend fun updateContact(contact: Contact): Int
    @Delete
    suspend fun deleteContact(contact: Contact): Int
    @Query("DELETE FROM `contacts`")
    suspend fun deleteAllContacts()
    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insertAll(contacts: List<Contact>)
    @Transaction // đảm bảo tất cả các truy vấn trong phương thức này được thực hiện một cách toàn vẹn, nếu 1 thao tác thất bại thì tất cả sẽ bị rollback
    @Query("SELECT * FROM contacts INNER JOIN contacts_groups_cross_ref ON contacts.id = contacts_groups_cross_ref.contactId WHERE contacts_groups_cross_ref.groupId = :groupId")
    suspend fun getContactsByGroupId(groupId: Int): List<Contact>
}