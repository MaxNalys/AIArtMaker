plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.aiartmaker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aiartmaker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Kotlin
    implementation(libs.kotlin.stdlib)

    // Core libraries
    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat)
    implementation (libs.material.v190)
    implementation (libs.androidx.constraintlayout)

    // ViewModel and LiveData
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")

    // Retrofit for API requests
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)

    // Coroutine support for Retrofit
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    // File Handling and URI Utils
    implementation (libs.androidx.documentfile)

    // Image Loading (Optional for loading images into ImageView)
    implementation (libs.glide)

    // Testing
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
}