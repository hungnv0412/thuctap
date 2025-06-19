package com.example.session3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.session3.Repository.ContactRepository
import com.example.session3.data.entity.Contact
import com.example.session3.data.entity.ContactGroupCrossRef
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> get() = _contacts
    private val _contact = MutableLiveData<Contact?>()
    val contact : LiveData<Contact?> = _contact

    private val ioScope : CoroutineScope = CoroutineScope(Job() + viewModelScope.coroutineContext)

    fun loadContacts() {
        ioScope.launch {
            _contacts.value=contactRepository.getContacts()
        }
    }

    fun addContact(contact: Contact) {
        ioScope.launch {
            contactRepository.addContact(contact)
            loadContacts()
        }
    }

    fun deleteContact(contact: Contact) {
        ioScope.launch {
            contactRepository.deleteContact(contact)
            loadContacts()
        }
    }
    fun searchContacts(name: String) {
        ioScope.launch {
            _contacts.value = contactRepository.searchContacts(name)
        }
    }
    fun getContactById(id: Int)  {
        ioScope.launch {
            _contact.value = contactRepository.getContactById(id)
        }
    }
    fun addContactToGroup(contactId: Int, groupId: Int) {
        ioScope.launch {
            contactRepository.addContactToGroup(ContactGroupCrossRef(contactId, groupId))
        }
    }
    fun getContactsByGroupId(groupId: Int) {
        ioScope.launch {
            _contacts.value = contactRepository.getContactWithGroup(groupId)
        }
    }
    fun deleteContactFromGroup(contactId : Int,groupId: Int){
        ioScope.launch{
            contactRepository.removeContactFromGroup(contactId,groupId)
            getContactsByGroupId(groupId)
        }
    }

    fun saveDraft(name : String ,
                  phoneNumber : String,
                  email : String,
                  note : String){
        savedStateHandle["name"] = name
        savedStateHandle["phoneNumber"] = phoneNumber
        savedStateHandle["email"] = email
        savedStateHandle["note"] = note
    }
    fun getDraftName(): String? = savedStateHandle["name"]
    fun getDraftPhoneNumber(): String? = savedStateHandle["phoneNumber"]
    fun getDraftEmail(): String? = savedStateHandle["email"]
    fun getDraftNote(): String? = savedStateHandle["note"]

    fun clearDraft() {
        savedStateHandle.remove<String>("name")
        savedStateHandle.remove<String>("phoneNumber")
        savedStateHandle.remove<String>("email")
        savedStateHandle.remove<String>("note")
        Log.d("ContactViewModel", "Draft cleared")
    }

    override fun onCleared() {
        super.onCleared()
        ioScope.cancel()
    }
}
