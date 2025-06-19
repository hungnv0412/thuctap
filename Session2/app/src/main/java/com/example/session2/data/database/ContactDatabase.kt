package com.example.session2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.session2.data.contact.Contact
import com.example.session2.data.contact.ContactDao
import com.example.session2.data.contactGroup.ContactGroupDao
import com.example.session2.data.contactGroup.ContactWithGroupRef
import com.example.session2.data.group.Group
import com.example.session2.data.group.GroupDao

@Database(entities = [Contact::class, Group::class, ContactWithGroupRef::class], version = 3, exportSchema = true)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun groupDao(): GroupDao
    abstract fun contactGroupDao(): ContactGroupDao
    companion object {
        private var INSTANCE: ContactDatabase? = null
        fun getInstance(context: Context): ContactDatabase {
            if (INSTANCE == null) {
                synchronized(ContactDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDatabase::class.java,
                        "contact_database"
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}