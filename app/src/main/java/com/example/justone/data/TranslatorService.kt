package com.example.justone.data

import retrofit2.http.Body
import retrofit2.http.POST

interface TranslatorService {

    @POST("translator")
    suspend fun translateWord(
        @Body request: WordTranslatorRequest
    ): WordTranslatorResponse
}
