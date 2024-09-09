package com.example.aiartmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.aiartmaker.viewmodel.ArtViewModel
import com.google.android.material.button.MaterialButton
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var artViewModel: ArtViewModel
    private var contentUri: Uri? = null
    private var styleUri: Uri? = null

    private lateinit var uploadPhotoButton: MaterialButton
    private lateinit var uploadStyleButton: MaterialButton
    private lateinit var generateArtButton: MaterialButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        artViewModel = ViewModelProvider(this).get(ArtViewModel::class.java)

        uploadPhotoButton = findViewById(R.id.upload_photo_button)
        uploadStyleButton = findViewById(R.id.upload_style_button)
        generateArtButton = findViewById(R.id.generate_art_button)

        val contentImagePicker: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            contentUri = uri
        }

        val styleImagePicker: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            styleUri = uri
        }

        uploadPhotoButton.setOnClickListener {
            contentImagePicker.launch("image/*")
        }

        uploadStyleButton.setOnClickListener {
            styleImagePicker.launch("image/*")
        }

        generateArtButton.setOnClickListener {
            if (contentUri != null && styleUri != null) {
                val contentImagePart = uriToMultipart(contentUri!!)
                val styleImagePart = uriToMultipart(styleUri!!)
                val focusContent = true // або false, залежно від вашого вибору

                // Очистка попередніх запитів
                artViewModel.clearPreviousRequests()

                // Відправка запиту до API
                artViewModel.generateArt(contentImagePart, styleImagePart, focusContent)
            } else {
                Toast.makeText(this, "Please select both images", Toast.LENGTH_SHORT).show()
            }
        }
        // Спостерігаємо за змінами у LiveData, щоб відобразити результат
        artViewModel.generatedArt.observe(this, Observer { artBase64 ->
            if (artBase64 != null) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java).apply {
                    putExtra("artBase64", artBase64)
                }
                startActivity(intent)
            } else {
                Log.e("MainActivity", "Error: No image generated")
            }
        })
    }

    private fun uriToMultipart(uri: Uri): MultipartBody.Part {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw Exception("Unable to open input stream for URI: $uri")
        val file = File.createTempFile("tempImage", null, cacheDir)
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}
