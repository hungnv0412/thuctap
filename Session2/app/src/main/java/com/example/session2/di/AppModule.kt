package com.example.session2.di

import android.content.Context
import com.example.session2.Repository.ContactRepository
import com.example.session2.data.contact.ContactDao
import com.example.session2.data.database.ContactDatabase
import com.example.session2.data.contactGroup.ContactGroupDao
import com.example.session2.data.group.GroupDao
import com.example.session2.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://663e6365e1913c4767978064.mockapi.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }
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
    fun  provideGroupDao(database: ContactDatabase): GroupDao {
        return database.groupDao()
    }
    @Provides
    @Singleton
    fun provideContactRepository(
        apiService: ApiService,
        contactDao: ContactDao,
        contactGroupDao: ContactGroupDao
    ): ContactRepository {
        return ContactRepository(apiService, contactDao, contactGroupDao)
    }
    @Provides
    @Singleton
    fun provideContactGroupDao(database: ContactDatabase): ContactGroupDao {
        return database.contactGroupDao()
    }
}