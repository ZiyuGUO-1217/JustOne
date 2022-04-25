package com.example.justone.generator.data

import javax.inject.Inject

class WordGeneratorRepository @Inject constructor(
    private val generatorService: GeneratorService
) {
    suspend fun getRandomWordList(wordsNumber: Int): com.example.foundation.network.ApiResult<List<String>> =
        com.example.foundation.network.callApi {
            val wordsResponse = generatorService.getRandomWords(
                table = "pictionary",
                language = "en",
                limit = wordsNumber,
                random = "yes"
            )
            return com.example.foundation.network.ApiResult.Success(wordsResponse.data)
        }
}
