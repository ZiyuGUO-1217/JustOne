package com.justone.domain.usecases

import com.justone.domain.utils.ClueUtils
import javax.inject.Inject

class DeduplicateClueUseCase @Inject constructor() {

    operator fun invoke(clues: List<String>, keyword: String): List<String> {
        val validClues = ClueUtils.checkClueValidation(clues, keyword)
        return ClueUtils.deduplicateClues(validClues)
    }
}
