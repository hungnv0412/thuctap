package com.example.session2.Repository

import android.util.Log
import com.example.session2.data.Contact
import com.example.session2.data.ContactDao
import javax.inject.Inject
import javax.inject.Singleton
import com.example.session2.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Singleton
class ContactRepository @Inject constructor(
    private val apiService: ApiService,
    private val contactDao: ContactDao
){
    suspend fun refreshContacts() : List<Contact> {
        return withContext(Dispatchers.IO) {
            try {
                val cachedContact = contactDao.getAllContacts()
                Log.d("ContactRepository", "Cached Contacts: ${cachedContact.size}")
                try {
                    val apiContact = apiService.getContacts()
                    Log.d("ContactRepository", "API Contacts: ${apiContact.size}")
                    contactDao.deleteAllContacts()
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
    suspend fun addContact(contact: Contact) {
        withContext(Dispatchers.IO) {
            try {
                apiService.createContact(contact)
                refreshContacts()
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error adding contact: ${e.message}")
            }
        }
    }
    suspend fun getContactById(id: Int): Contact? {
        return withContext(Dispatchers.IO) {
            contactDao.getContactById(id)
        }
    }
    suspend fun deleteContact(id: Int) {
        withContext(Dispatchers.IO) {
            try {
                apiService.deleteContact(id)
                refreshContacts()
            } catch (e: Exception) {
                Log.e("ContactRepository", "Error deleting contact: ${e.message}")
            }
        }
    }

}