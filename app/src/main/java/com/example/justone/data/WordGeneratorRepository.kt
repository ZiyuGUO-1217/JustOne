package com.example.justone.data

import com.example.justone.network.ApiResult
import com.example.justone.network.callApi
import javax.inject.Inject

class WordGeneratorRepository @Inject constructor(
    private val generatorService: GeneratorService
) {
    suspend fun getRandomWordList(wordsNumber: Int): ApiResult<List<String>> = callApi {
        val wordsResponse = generatorService.getRandomWords(
            table = "pictionary",
            language = "en",
            limit = wordsNumber,
            random = "yes"
        )
        return ApiResult.Success(wordsResponse.data)
    }
}
