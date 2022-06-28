package com.justone.domain.usecases

import com.justone.domain.utils.ClueUtils
import javax.inject.Inject

class CheckAnswerUseCase @Inject constructor() {

    operator fun invoke(answer: String, guess: String) =
        ClueUtils.isSameClue(answer, guess) && guess.isNotBlank()
}
