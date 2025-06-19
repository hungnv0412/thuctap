package com.example.session3.Repository

import android.util.Log
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.entity.Contact
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.entity.ContactGroupCrossRef
import com.example.session3.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val contactDao: ContactDao,
    private val apiService: ApiService,
    private val contactGroupDao: ContactGroupDao
) {
    suspend fun addContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            apiService.createContact(contact)
            contactDao.insertContact(contact)
        }
    }
    suspend fun getContacts() : List<Contact> {
        return withContext(Dispatchers.IO) {
            try {
                val cachedContact = contactDao.getAllContacts()
                Log.d("ContactRepository", "Cached Contacts: ${cachedContact.size}")
                try {
                    val apiContact = apiService.getContacts()
                    Log.d("ContactRepository", "API Contacts: ${apiContact.size}")
                    contactDao.insertAll(apiContact)

                    apiContact
                }
                catch (e: Exception) {
                    Log.e("ContactRepository", "Error fetching contacts from API: ${e.message}")
                    cachedContact
                }
            }catch (e: Exception) {
                Log.e("ContactRepository", "Error fetching contacts from database: ${e.message}")
                emptyList<Contact>()
            }
        }
    }

    suspend fun getContactById(id: Int): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(id)
        }
    }

    suspend fun deleteContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            apiService.deleteContact(contact.id)
            val result = contactDao.deleteContact(contact)
            if (result > 0) {
                Log.d("ContactRepository", "Contact deleted successfully")
            } else {
                Log.e("ContactRepository", "Failed to delete contact")
            }
        }
    }
    suspend fun searchContacts(name : String): List<Contact> {
        return withContext(Dispatchers.IO) {
            try {
                val apiContacts = apiService.searchContacts(name)
                Log.d("ContactRepository", "API Search Contacts: ${apiContacts.size}")
                apiContacts
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error searching contacts from API: ${e.message}")
                emptyList<Contact>()
            }
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
