package com.justone.foundation.useCase

import com.justone.foundation.network.ApiResult
import com.justone.foundation.network.ErrorType
import java.io.IOException
import retrofit2.HttpException

abstract class BaseCoUseCase<P, R> {
    suspend operator fun invoke(parameters: P): ApiResult<R> {
        return try {
            // Moving all use case's executions to the injected dispatcher
            // In production code, this is usually the Default dispatcher (background thread)
            // In tests, this becomes a TestCoroutineDispatcher
            ApiResult.Success(execute(parameters))
        } catch (e: Throwable) {
            when (e) {
                is IOException -> ApiResult.Error(ErrorType.NetworkError)
                is HttpException -> {
                    println("callApiError:" + "${e.message}")
                    ApiResult.Error(ErrorType.ServerError)
                }
                else -> ApiResult.Error(ErrorType.ServerError)
            }
        }
    }

    protected abstract suspend fun execute(parameters: P): R
}
