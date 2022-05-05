package com.example.foundation.network

import com.example.foundation.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val GENERATOR_READ_TIMEOUT = 7_000L
private const val GENERATOR_WRITE_TIMEOUT = 7_000L

@Module
@InstallIn(SingletonComponent::class)
object GeneratorNetworkModule {

    @Provides
    @Named("WordsGenerator")
    @Singleton
    fun provideRetrofit(
        @Named("WordsGenerator") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://words-generator.p.rapidapi.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Named( "WordsGenerator")
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BASIC)
        }

        return OkHttpClient.Builder()
            .readTimeout(GENERATOR_READ_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(GENERATOR_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
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
