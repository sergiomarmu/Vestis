package com.vestis.data.profile.network.api

import com.vestis.data.profile.network.dto.ProfileDTO
import retrofit2.Response
import retrofit2.http.GET

interface ProfileApiService {

    @GET("/users/8")
    suspend fun getUser(): Response<ProfileDTO>
}