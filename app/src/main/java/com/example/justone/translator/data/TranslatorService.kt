package com.example.justone.translator.data

import retrofit2.http.Body
import retrofit2.http.POST

interface TranslatorService {

    @POST("translator")
    suspend fun translateWord(
        @Body request: WordTranslateRequest
    ): String
}
