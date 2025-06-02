package com.example.session3.di

import android.content.Context
import com.example.session3.Repository.ContactRepository
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.DAO.GroupDao
import com.example.session3.data.Database.ContactDatabase
import com.example.session3.sharedPreferences.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getInstance(context)// Ensure the database is built correctly
    }

    @Provides
    @Singleton
    fun provideContactDao(database: ContactDatabase): ContactDao {
        return database.contactDao()
    }
    @Provides
    @Singleton
    fun provideGroupDao(database: ContactDatabase): GroupDao{
        return database.groupDao()
    }
    @Provides
    @Singleton
    fun provideContactGroupDao(database: ContactDatabase): ContactGroupDao{
        return database.contactGroupDao()
    }
}
