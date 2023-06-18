package com.duhapp.dnotes.features.base.domain

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> Result<T>.isLoading(): Boolean {
    return this.isSuccess && this.getOrNull() == null
}

fun <T> Result<T>.setLoading(): Result<T> {
    this.map {
        null
    }
    return this
}

fun <T> MutableStateFlow<Result<T>?>.setLoading() {
    this.value = this.value?.setLoading()
}

fun <US> MutableStateFlow<Result<US>?>.isLoading(): Boolean {
    return this.value?.isLoading() ?: false
}