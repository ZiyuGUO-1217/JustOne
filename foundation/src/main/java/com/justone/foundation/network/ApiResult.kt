package com.justone.foundation.network

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val errorType: ErrorType) : ApiResult<Nothing>()
}

sealed interface ErrorType {
    object NetworkError : ErrorType
    object ServerError : ErrorType
}

suspend fun <T> ApiResult<T>.onSuccess(action: suspend (T) -> Unit): ApiResult<T> {
    if (this.isSuccess()) action(this.data)
    return this
}

suspend fun <T> ApiResult<T>.onError(action: suspend (ErrorType) -> Unit): ApiResult<T> {
    if (this.isError()) action(this.errorType)
    return this
}


@OptIn(ExperimentalContracts::class)
fun <T> ApiResult<T>.isSuccess(): Boolean {
    contract {
        returns(true) implies (this@isSuccess is ApiResult.Success)
    }
    return this is ApiResult.Success
}


@OptIn(ExperimentalContracts::class)
fun <T> ApiResult<T>.isError(): Boolean {
    contract {
        returns(true) implies (this@isError is ApiResult.Error)
    }
    return this is ApiResult.Error
}
