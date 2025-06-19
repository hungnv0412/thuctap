package com.example.session3.network

import com.example.session3.data.entity.Contact
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface ApiService {
    @GET("contact")
    suspend fun getContacts(): List<Contact>
    @POST("contact")
    suspend fun createContact(@Body contact: Contact): Contact
    @PUT("contact/{id}")
    suspend fun updateContact(@Path("id") id: Int, @Body contact: Contact): Contact
    @DELETE("contact/{id}")
    suspend fun deleteContact(@Path("id") id: Int): Contact
    @GET("contact")
    suspend fun searchContacts(@Query("name") name: String): List<Contact>
}