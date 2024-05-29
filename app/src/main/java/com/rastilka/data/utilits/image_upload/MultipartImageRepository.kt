package com.rastilka.data.utilits.image_upload

import android.net.Uri
import okhttp3.MultipartBody

interface MultipartImageRepository {
    fun createMultipartImage(selectedImage: Uri?, nameUploadImage: String): MultipartBody.Part?
}