package com.example.session2.network

import com.example.session2.data.Contact
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("contact")
    suspend fun getContacts(): List<Contact>
    @POST("contact")
    suspend fun createContact(@Body contact: Contact): Contact
    @GET("contact/{id}")
    suspend fun getContactById(@Path("id") id: Int): Contact
    @DELETE("contact/{id}")
    suspend fun deleteContact(@Path("id") id: Int): Contact
}