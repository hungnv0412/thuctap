package com.example.session3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.session3.Repository.ContactRepository
import com.example.session3.data.Entity.Contact
import com.example.session3.data.Entity.ContactGroupCrossRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository

) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts
    private val _contact = MutableLiveData<Contact?>()
    val contact : LiveData<Contact?> = _contact

    fun loadContacts() {
        viewModelScope.launch {
            _contacts.value = contactRepository.getContacts() // Ensure data is loaded
        }
    }

    fun addContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.addContact(contact)
            loadContacts() // Refresh data after adding
        }
    }

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            contactRepository.deleteContact(contact)
            loadContacts() // Refresh data after deleting
        }
    }

    fun getContactById(id: Int)  {
        viewModelScope.launch {
            _contact.value = contactRepository.getContactById(id)
        }
    }
    fun addContactToGroup(contactId: Int, groupId: Int) {
        viewModelScope.launch {
            contactRepository.addContactToGroup(ContactGroupCrossRef(contactId, groupId))
        }
    }
    fun getContactsByGroupId(groupId: Int) {
        viewModelScope.launch {
            _contacts.value = contactRepository.getContactWithGroup(groupId)
        }
    }
}
