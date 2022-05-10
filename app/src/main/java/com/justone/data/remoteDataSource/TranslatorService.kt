package com.justone.data.remoteDataSource

import com.justone.data.model.WordTranslatorRequest
import com.justone.data.model.WordTranslatorResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslatorService {

    @POST("translator")
    suspend fun translateWord(
        @Body request: WordTranslatorRequest
    ): WordTranslatorResponse
}
