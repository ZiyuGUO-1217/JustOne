package com.justone.foundation.useCase

import com.justone.foundation.network.ApiResult
import com.justone.foundation.network.ResourceState
import com.justone.foundation.network.onError
import com.justone.foundation.network.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

abstract class BaseFlowUseCase<P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(parameter: P): Flow<ResourceState<R>> {
        return flow {
            execute(parameter)
                .onSuccess { emit(ResourceState.Success(it)) }
                .onError { emit(ResourceState.Error(it)) }
        }.flowOn(coroutineDispatcher)
    }

    abstract suspend fun execute(parameter: P): ApiResult<R>
}
