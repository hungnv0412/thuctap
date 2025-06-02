package com.example.session2.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.session2.data.Contact
import com.example.session2.Repository.ContactRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _isloading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isloading
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts
    fun refreshContacts() {
        viewModelScope.launch {
            try {
                _isloading.postValue(true)
                contactRepository.refreshContacts()
                _contacts.value = contactRepository.refreshContacts()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isloading.postValue(false)
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
}
