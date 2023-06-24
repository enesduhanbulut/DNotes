package com.duhapp.dnotes.features.base.domain

import androidx.annotation.StringRes
import com.duhapp.dnotes.R

sealed class CustomException : Exception() {
    data class DatabaseException(val data: CustomExceptionData) : CustomException()
    data class NetworkException(val data: CustomExceptionData) : CustomException()
    data class UnknownException(val data: CustomExceptionData) : CustomException()
    data class AuthenticationException(val data: CustomExceptionData) : CustomException()
    data class ServerException(val data: CustomExceptionData) : CustomException()
    data class UnknownNetworkException(val data: CustomExceptionData) : CustomException()
    data class UnknownDatabaseException(val data: CustomExceptionData) : CustomException()
    data class ThereIsNoSuitableVariableException(val data: CustomExceptionData) : CustomException()
}

data class CustomExceptionData(
    @StringRes val title: Int = R.string.Error,
    @StringRes val message: Int = R.string.An_error_occurred_please_try_again_later,
    val code: Int,
    val image: Int = com.google.android.material.R.drawable.mtrl_ic_error
)
