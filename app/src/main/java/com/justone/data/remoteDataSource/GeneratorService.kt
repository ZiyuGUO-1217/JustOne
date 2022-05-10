package com.justone.data.remoteDataSource

import com.justone.data.model.WordGeneratorResponse
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
