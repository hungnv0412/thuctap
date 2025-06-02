package com.example.session3.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.DAO.GroupDao
import com.example.session3.data.Entity.Contact
import com.example.session3.data.Entity.ContactGroupCrossRef
import com.example.session3.data.Entity.ContactWithGroups
import com.example.session3.data.Entity.Group

@Database(entities = [Contact::class,Group::class,ContactGroupCrossRef::class], version = 2, exportSchema = true)
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