package com.justone.foundation.useCase

abstract class BaseUseCase<in P, R> {
    operator fun invoke(parameter: P): R {
        return execute(parameter)
    }

    abstract fun execute(parameter: P): R
}
