package com.justone.foundation.network

sealed interface ResourceState<out T> {
    object Loading : ResourceState<Nothing>
    object Empty: ResourceState<Nothing>
    data class Success<T>(val data: T) : ResourceState<T>
    data class Error(val error: ErrorType) : ResourceState<Nothing>
}
