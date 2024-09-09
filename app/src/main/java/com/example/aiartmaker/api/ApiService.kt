package com.example.aiartmaker.api

import com.example.aiartmaker.model.ArtResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("/remix-art")
    fun generateArt(
        @Part contentImage: MultipartBody.Part,
        @Part styleImage: MultipartBody.Part,
        @Part focusContent: MultipartBody.Part
    ): Call<ArtResponse>
}
