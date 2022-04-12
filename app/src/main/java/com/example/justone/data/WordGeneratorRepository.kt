package com.example.justone.data

import com.example.justone.network.ApiResult
import com.example.justone.network.callApi
import javax.inject.Inject

class WordGeneratorRepository @Inject constructor(
    private val remoteDateSource: RemoteDateSource
) {
    suspend fun getRandomWordList(): ApiResult<List<String>> = callApi {
        val wordsResponse = remoteDateSource.getRandomWords(table = "pictionary", language = "en", limit = 5, isRandom = "yes")
        return ApiResult.Success(wordsResponse.data)
    }
}