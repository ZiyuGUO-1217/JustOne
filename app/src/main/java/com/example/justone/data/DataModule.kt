package com.example.justone.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideGeneratorService(
        @Named("WordsGenerator") retrofit: Retrofit
    ): GeneratorService =
        retrofit.create(GeneratorService::class.java)

    @Provides
    fun provideTranslatorService(
        @Named("WordsTranslator") retrofit: Retrofit
    ): TranslatorService =
        retrofit.create(TranslatorService::class.java)
}
