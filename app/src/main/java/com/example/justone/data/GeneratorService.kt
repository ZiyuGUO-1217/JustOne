package com.example.justone.data

import retrofit2.http.GET
import retrofit2.http.Query

interface GeneratorService {

    @GET("?")
    suspend fun getRandomWords(
        @Query("table") table: String,
        @Query("lang") language: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("random") random: String? = null
    ): WordGeneratorResponse
}
