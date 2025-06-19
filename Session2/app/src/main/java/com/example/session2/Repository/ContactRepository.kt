package com.example.session2.Repository

import android.util.Log
import com.example.session2.data.contact.Contact
import com.example.session2.data.contact.ContactDao
import com.example.session2.data.contact.ContactUpdatePhoneNumber
import com.example.session2.data.contactGroup.ContactGroupDao
import com.example.session2.data.contactGroup.ContactWithGroupRef
import com.example.session2.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepository @Inject constructor(
    private val apiService: ApiService,
    private val contactDao: ContactDao,
    private val contactGroupDao: ContactGroupDao
) {
    suspend fun refreshContacts(): List<Contact> {
        return withContext(Dispatchers.IO) {
            val cached = contactDao.getAllContacts()
            try {
                val response = apiService.getContacts()
                if (response.isSuccessful) {
                    val contacts = response.body() ?: emptyList()
                    contactDao.insertAll(contacts)
                    Log.d("ContactRepository", "Loaded ${contacts.size} contacts from API")
                    contacts
                } else {
                    Log.e("ContactRepository", "API error: ${response.code()}")
                    cached
                }
            } catch (e: Exception) {
                Log.e("ContactRepository", "Network error: ${e.message}")
                cached
            }
        }
    }

    suspend fun addContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.createContact(contact)
                if (response.isSuccessful) {
                    refreshContacts()
                } else {
                    Log.e("ContactRepository", "Failed to create contact: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error creating contact: ${e.message}")
            }
        }
    }

    suspend fun getContactById(id: Int): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(id)
        }
    }

    suspend fun searchContacts(name: String): List<Contact> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.searchContacts(name)
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("ContactRepository", "Search error: ${response.code()}")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error searching contacts: ${e.message}")
                emptyList()
            }
        }
    }
    suspend fun updateContactPhoneNumber(id: Int, phoneNumber: String) {
        withContext(Dispatchers.IO){
            try {
                val response = apiService.updateContactPhoneNumber(id, ContactUpdatePhoneNumber(phoneNumber))
                if (response.isSuccessful) {
                    refreshContacts()
                } else {
                    Log.e("ContactRepository", "Update phone number failed: ${response.code()}")
                }
            }catch (e: Exception) {
                Log.e("ContactRepository", "Error updating phone number: ${e.message}")
            }
        }
    }

    suspend fun deleteContact(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteContact(id)
                if (response.isSuccessful) {
                    refreshContacts()
                } else {
                    Log.e("ContactRepository", "Delete failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error deleting contact: ${e.message}")
            }
        }
    }

    suspend fun addContactToGroup(contactId: Int, groupId: Int) {
        withContext(Dispatchers.IO) {
            try {
                contactGroupDao.addContactToGroup(ContactWithGroupRef(contactId, groupId))
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error adding contact to group: ${e.message}")
            }
        }
    }

    suspend fun getContactsByGroupId(groupId: Int): List<Contact> {
        return withContext(Dispatchers.IO) {
            try {
                contactDao.getContactsByGroupId(groupId)
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error fetching group contacts: ${e.message}")
                emptyList()
            }
        }
    }
}
