package com.users.Api

import com.users.model.UserDetailsResponse
import com.users.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/users?")
    suspend fun getUsers(@Query("per_page") per_page: Int): Response<UserResponse>

    @GET("/api/users/{id}")
    suspend fun getUserDetails(@Path("id") id: Int): Response<UserDetailsResponse>

}