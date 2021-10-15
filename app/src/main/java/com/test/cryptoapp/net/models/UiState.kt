package com.test.cryptoapp.net.models

sealed class UiState<out T : Any> {

    object Loading : UiState<Nothing>()

    class Error(val error: String) : UiState<Nothing>()

    class Success<out T : Any>(val data: T) : UiState<T>()
}
