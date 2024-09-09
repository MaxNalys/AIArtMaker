package com.example.aiartmaker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.aiartmaker.api.RetrofitInstance
import com.example.aiartmaker.model.ArtResponse
import com.example.aiartmaker.repository.ArtRepository
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArtViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ArtRepository(RetrofitInstance.api)
    private var currentCall: Call<ArtResponse>? = null

    private val _generatedArt = MutableLiveData<String?>()
    val generatedArt: MutableLiveData<String?> get() = _generatedArt

    fun generateArt(contentImage: MultipartBody.Part, styleImage: MultipartBody.Part, focusContent: Boolean) {
        // Скасування попереднього запиту, якщо він є
        currentCall?.cancel()

        // Виконання нового запиту
        currentCall = repository.generateArt(contentImage, styleImage, focusContent).apply {
            enqueue(object : Callback<ArtResponse> {
                override fun onResponse(call: Call<ArtResponse>, response: Response<ArtResponse>) {
                    if (response.isSuccessful) {
                        val artResponse = response.body()
                        Log.d("ArtViewModel", "Response body: $artResponse")

                        if (artResponse?.imageBase64 != null) {
                            _generatedArt.value = artResponse.imageBase64
                        } else {
                            Log.e("ArtViewModel", "Error: No imageBase64 in response")
                            _generatedArt.value = null
                        }
                    } else {
                        Log.e("ArtViewModel", "Error: ${response.errorBody()?.string()}")
                        _generatedArt.value = null
                    }
                }

                override fun onFailure(call: Call<ArtResponse>, t: Throwable) {
                    if (t is java.io.IOException && !call.isCanceled) {
                        Log.e("ArtViewModel", "Failed: ${t.message}")
                    }
                    _generatedArt.value = null
                }
            })
        }
    }


    fun clearPreviousRequests() {
        currentCall?.cancel()
    }
}
