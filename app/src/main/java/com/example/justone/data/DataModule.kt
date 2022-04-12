package com.example.justone.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): RemoteDateSource =
        retrofit.create(RemoteDateSource::class.java)
}