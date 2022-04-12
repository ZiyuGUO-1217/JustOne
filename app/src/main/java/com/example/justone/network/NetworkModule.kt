package com.example.justone.network

import com.example.justone.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://words-generator.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

        return OkHttpClient.Builder()
            .readTimeout(5_000L, TimeUnit.MILLISECONDS)
            .writeTimeout(5_000L, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()
                builder.header("X-RapidAPI-Host", "words-generator.p.rapidapi.com")
                builder.header("X-RapidAPI-Key", BuildConfig.API_KEY)
                return@addInterceptor chain.proceed(builder.build())
            }
            .addInterceptor(logging)
            .build()
    }
}
