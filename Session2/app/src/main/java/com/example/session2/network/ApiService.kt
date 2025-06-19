package com.example.session2.network

import com.example.session2.data.contact.Contact
import com.example.session2.data.contact.ContactUpdatePhoneNumber
import com.example.session2.data.group.Group
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("contact")
    suspend fun getContacts(): Response<List<Contact>>

    @POST("contact")
    suspend fun createContact(@Body contact: Contact): Response<Contact>

    @PUT
    suspend fun updateContact(@Body contact: Contact , @Path("id") id: Int): Response<Contact>

    @GET("contact")
    suspend fun getWithHeader(@Header("Authorization") token: String): Response<List<Contact>>

    @PATCH
    suspend fun updateContactPhoneNumber(
        @Path("id") id: Int,
        @Body phoneNumber: ContactUpdatePhoneNumber
    ): Response<Contact>

    @FormUrlEncoded
    @POST
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<String>

    @Multipart
    @POST("contact/upload")
    suspend fun uploadContactImage(
        @Part("id") id: Int,
        @Part image: MultipartBody.Part
    ): Response<Contact>

    @Streaming
    @GET("download/{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): Response<ResponseBody>

    @DELETE("contact/{id}")
    suspend fun deleteContact(@Path("id") id: Int): Response<Contact>

    @GET("contact")
    suspend fun searchContacts(@Query("name") name: String): Response<List<Contact>>


    @GET("groups")
    suspend fun getGroups(): Response<List<Group>>

    @POST("groups")
    suspend fun createGroup(@Body group: Group): Response<Group>

    @DELETE("groups/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): Response<Group>
}
