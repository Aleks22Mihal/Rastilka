package com.rastilka.di.network_module

import com.rastilka.BuildConfig
import com.rastilka.data.data_source.Internal_storage.DataPreferences
import com.rastilka.data.data_source.remote.ApiService
import com.rastilka.data.repository.MainRepositoryImpl
import com.rastilka.domain.repository.MainRepository
import com.rastilka.domain.utilits.image_upload.ImageUpload
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(dataPreferences: DataPreferences): OkHttpClient {

        val loginInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val origin = chain.request()
                val requestBuilder = origin.newBuilder()
                    .addHeader(
                        name = "Authorization",
                        value = runBlocking { "BearerSession ${dataPreferences.session.first()}" }
                    )
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addNetworkInterceptor(loginInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    }

    @Provides
    @Singleton
    fun provideRestClient(client: OkHttpClient): ApiService {

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainRepository(apiService: ApiService, imageUpload: ImageUpload): MainRepository {
        return MainRepositoryImpl(apiService, imageUpload)
    }
}