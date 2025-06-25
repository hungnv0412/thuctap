package com.example.session2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.session2.data.contact.Contact
import com.example.session2.Repository.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _isloading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isloading
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts
    init {
        refreshContacts()
    }
    fun refreshContacts() {
        viewModelScope.launch {
            try {
                _isloading.value = true
                _contacts.value = contactRepository.refreshContacts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isloading.value= false
            }
        }
    }
    fun createContact(contact: Contact) {
        viewModelScope.launch {
            try {
                contactRepository.addContact(contact)
                refreshContacts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getContactById(id: Int): Contact? {
        return _contacts.value?.find { it.id == id }
    }
    fun deleteContact(id: Int) {
        viewModelScope.launch {
            try {
                contactRepository.deleteContact(id)
                refreshContacts()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun searchContacts(name: String){
        viewModelScope.launch {
            try {
                _isloading.value = true
                _contacts.value = contactRepository.searchContacts(name)
            }catch (e: Exception) {
                Log.e("ContactViewModel", "Error searching contacts: ${e.message}")
            }
            finally {
                _isloading.value = false
            }
        }
    }
    fun addContactToGroup(contactId: Int, groupId: Int) {
        viewModelScope.launch {
            try {
                contactRepository.addContactToGroup(contactId, groupId)
            } catch (e: Exception) {
                Log.e("ContactViewModel", "Error adding contact to group: ${e.message}")
            }
        }
    }
    fun updateContactPhoneNumber(id: Int, phoneNumber: String) {
        viewModelScope.launch {
            try {
                contactRepository.updateContactPhoneNumber(id, phoneNumber)
                refreshContacts()
            } catch (e: Exception) {
                Log.e("ContactViewModel", "Error updating phone number: ${e.message}")
            }
        }
    }
    fun getContactsByGroupId(groupId: Int) {
        viewModelScope.launch {
            _contacts.value = contactRepository.getContactsByGroupId(groupId)
        }
    }
    fun loadContactUsingAsync(){
        viewModelScope.launch {
            _isloading.value= true
            try {
                val contactDeferred = async { contactRepository.refreshContacts() }
                _contacts.value = contactDeferred.await()
            }catch (e: Exception) {
                Log.e("ContactViewModel", "Error loading contacts: ${e.message}")
            } finally {
                _isloading.value = false
            }
        }
    }
}
