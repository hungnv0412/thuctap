package com.example.session3.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.DAO.GroupDao
import com.example.session3.data.database.migrations.MIGRATION_2_3
import com.example.session3.data.entity.Contact
import com.example.session3.data.entity.ContactGroupCrossRef
import com.example.session3.data.entity.Group

@Database(entities = [Contact::class,Group::class,ContactGroupCrossRef::class], version = 3, exportSchema = true)
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
                    ).addMigrations(MIGRATION_2_3).build()
                }
            }
            return INSTANCE!!
        }
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}