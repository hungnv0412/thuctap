package com.example.session2.Repository

import com.example.session2.model.Contact
import javax.inject.Inject
import javax.inject.Singleton
import com.example.session2.R
@Singleton
class ContactRepository @Inject constructor(){
    private val contacts = mutableListOf(
        Contact(1,"John Doe", "123456789", R.drawable.avt2, "john.doe@example.com", "Friend"),
        Contact(2,"Jane Smith", "987654321", R.drawable.avt2, "jane.smith@example.com", "Colleague"),
        Contact(3,"Alice Brown", "555666777", R.drawable.avt2, "alice.brown@example.com", "Family"),
        Contact(4,"Bob Johnson", "444333222", R.drawable.avt2, "bob.johnson@example.com", "Neighbor"),
        Contact(5,"nvh", "12345", R.drawable.avt2, "@gmail.com", "note here"),
    )
    private var nextId: Int = contacts.maxOfOrNull { it.id }?.plus(1) ?: 1
    fun addContact(contact: Contact) {
        val newContact = contact.copy(id = nextId)
        contacts.add(newContact)
        nextId++
    }
    fun getContacts(): List<Contact> {
        return contacts
    }
    fun getContactById(id: Int): Contact? {
        return contacts.find { it.id == id }
    }
    fun deleteContact(contact: Contact) {
        contacts.remove(contact)
    }
}