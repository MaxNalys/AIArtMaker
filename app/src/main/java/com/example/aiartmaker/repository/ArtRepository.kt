package com.example.aiartmaker.repository

import com.example.aiartmaker.api.ApiService
import com.example.aiartmaker.model.ArtResponse
import okhttp3.MultipartBody
import retrofit2.Call

class ArtRepository(private val apiService: ApiService) {

    fun generateArt(contentImage: MultipartBody.Part, styleImage: MultipartBody.Part, focusContent: Boolean): Call<ArtResponse> {
        val focusContentRequest = MultipartBody.Part.createFormData("focusContent", focusContent.toString())

        return apiService.generateArt(contentImage, styleImage, focusContentRequest)
    }

}
