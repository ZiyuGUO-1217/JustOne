package com.justone.data.ble

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BLEModule {

    @Singleton
    @Provides
    fun provideBLEManager(@ApplicationContext context: Context): BLEManager {
        return BLEManager(context)
    }
}
