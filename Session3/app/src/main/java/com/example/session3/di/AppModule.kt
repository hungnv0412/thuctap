package com.example.session3.di

import android.content.Context
import com.example.session3.data.DAO.ContactDao
import com.example.session3.data.DAO.ContactGroupDao
import com.example.session3.data.DAO.GroupDao
import com.example.session3.data.database.ContactDatabase
import com.example.session3.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ContactDatabase {
        return ContactDatabase.getInstance(context)
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
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://663e6365e1913c4767978064.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideContactRepository(
        contactDao: ContactDao,
        apiService : ApiService,
        contactGroupDao: ContactGroupDao
    ): com.example.session3.Repository.ContactRepository {
        return com.example.session3.Repository.ContactRepository(contactDao, apiService, contactGroupDao)
    }
}
