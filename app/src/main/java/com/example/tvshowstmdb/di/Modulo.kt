package com.example.tvshowstmdb.di

import com.example.tvshowstmdb.data.model.remote.ServiceApi
import com.example.tvshowstmdb.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Modulo {

    @Singleton
    @Provides
    fun provedorOkHttpClint(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer ${Constants.ACCESS_TOKEN}")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @Singleton
    @Provides
    fun provedorRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provedorService(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)

    }
}