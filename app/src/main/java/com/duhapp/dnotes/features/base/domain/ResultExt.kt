package com.duhapp.dnotes.features.base.domain

import androidx.annotation.StringRes
import com.duhapp.dnotes.R
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


fun Exception.asCustomException(
    @StringRes title: Int = R.string.Unknown_Error,
    @StringRes message: Int = R.string.Unknown_Error_Message,
    code: Int = -1,
    image: Int = com.google.android.material.R.drawable.mtrl_ic_error
): CustomException {
    return when (this) {
        is CustomException -> this
        else -> {
            CustomException.UnknownException(
                CustomExceptionData(
                    title = title,
                    message = message,
                    code = code,
                    image = image
                )
            )
        }
    }
}