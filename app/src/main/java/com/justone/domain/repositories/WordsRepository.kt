package com.justone.domain.repositories

interface WordsRepository {
    suspend fun getRandomWordList(wordsNumber: Int): List<String>
    suspend fun translateWord(word: String): String
}
