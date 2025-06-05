package com.example.session3.Repository

import com.example.session3.data.Entity.Contact
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.Entity.ContactGroupCrossRef
import com.example.session3.data.Entity.ContactWithGroups
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao,
    private val contactGroupDao: ContactGroupDao
) {
    suspend fun addContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.insertContact(contact)
        }
    }

    suspend fun getContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            contactDao.getAllContacts() // Fetch directly from the database
        }
    }

    suspend fun getContactById(id: Int): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(id)
        }
    }

    suspend fun deleteContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            contactDao.deleteContact(contact)
        }
    }
    suspend fun addContactToGroup(contactGroup: ContactGroupCrossRef){
        withContext(Dispatchers.IO){
            contactGroupDao.addContactToGroup(contactGroup)
        }
    }
    suspend fun getContactWithGroup(contactId: Int): List<Contact> {
        return withContext(Dispatchers.IO) {
            contactDao.getContactsByGroupId(contactId)
        }
    }
    suspend fun removeContactFromGroup(contactId: Int, groupId: Int) {
        withContext(Dispatchers.IO) {
            contactGroupDao.deleteContactFromGroup(contactId, groupId)
        }
    }
}
