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

@Module
@InstallIn(SingletonComponent::class)
object TranslatorNetworkModule {

    @Provides
    @Named("WordsTranslator")
    @Singleton
    fun provideRetrofit(
        @Named("WordsTranslator") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://api.interpreter.caiyunai.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Named("WordsTranslator")
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
                builder.header("content-type", "application/json")
                builder.header("x-authorization", "token " + BuildConfig.TRANSLATION_API_KEY)
                return@addInterceptor chain.proceed(builder.build())
            }
            .addInterceptor(logging)
            .build()
    }
}
