package com.example.session3.Repository

import com.example.session3.data.Contact
import com.example.session3.data.ContactDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao
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
}
