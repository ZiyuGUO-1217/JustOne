package com.justone.domain.usecases

import com.justone.foundation.useCase.BaseUseCase
import javax.inject.Inject

class CheckPlayerNumberUseCase @Inject constructor() : BaseUseCase<Int, Boolean>() {
    companion object {
        const val MINIMUM_PLAYER_NUMBER: Int = 2
    }

    override fun execute(parameter: Int): Boolean {
        return parameter >= MINIMUM_PLAYER_NUMBER
    }
}
