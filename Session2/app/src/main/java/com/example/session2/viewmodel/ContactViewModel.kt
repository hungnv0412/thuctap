package com.example.session2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.session2.model.Contact
import com.example.session2.Repository.ContactRepository
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    // Inject any dependencies you need here
    private val contactRepository: ContactRepository
) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>(contactRepository.getContacts().toMutableList())
    val contacts: LiveData<List<Contact>> get() = _contacts
    fun addContact(contact: Contact) {
        contactRepository.addContact(contact)
        _contacts.value = contactRepository.getContacts()
    }
    fun getContactById(id: Int): Contact? {
        return contactRepository.getContactById(id)
    }
    fun deleteContact(contact: Contact) {
        contactRepository.deleteContact(contact)
        _contacts.value = contactRepository.getContacts()
    }
    fun getContacts(): List<Contact> {
        return contactRepository.getContacts()
    }


}
