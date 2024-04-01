package com.rastilka.data.utilits.image_upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageUpload @Inject constructor(
    @ApplicationContext private val appContext: Context
): MultipartImageRepository {

    override fun createMultipartImage(selectedImage: Uri?): MultipartBody.Part? {
        return if (selectedImage != null) {
            val imageBitmap: Bitmap? = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(appContext.contentResolver, selectedImage)
            } else {
                val source = ImageDecoder.createSource(appContext.contentResolver, selectedImage)
                ImageDecoder.decodeBitmap(source)
            }
            val bitmapArrayOutput = ByteArrayOutputStream()
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, bitmapArrayOutput)
            val bitmapData: ByteArray = bitmapArrayOutput.toByteArray()
            val file = File(appContext.cacheDir, "image.png")
            file.createNewFile()
            file.outputStream().use { outputStream ->
                outputStream.write(bitmapData)
                outputStream.flush()
                outputStream.close()
            }
            val requestFile = file.asRequestBody(
                appContext.contentResolver.getType(selectedImage)?.toMediaTypeOrNull()
            )
            MultipartBody.Part.createFormData(
                "picture",
                file.name,
                requestFile
            )
        } else {
            null
        }
    }

}