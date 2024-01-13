package com.duhapp.dnotes.features.base.domain

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.duhapp.dnotes.R

sealed class CustomException(
    open val data: CustomExceptionData
) : Exception() {
    data class DatabaseException(override val data: CustomExceptionData) : CustomException(data)
    data class NetworkException(override val data: CustomExceptionData) : CustomException(data)
    data class UnknownException(override val data: CustomExceptionData) : CustomException(data)
    data class AuthenticationException(override val data: CustomExceptionData) : CustomException(data)
    data class ServerException(override val data: CustomExceptionData) : CustomException(data)
    data class UnknownNetworkException(override val data: CustomExceptionData) : CustomException(data)
    data class UnknownDatabaseException(override val data: CustomExceptionData) : CustomException(data)
    data class ThereIsNoSuitableVariableException(override val data: CustomExceptionData) : CustomException(data)
    data class UndoUnavailableException(override val data: CustomExceptionData) : CustomException(data)
    data class NotValidParametersException(override val data: CustomExceptionData) : CustomException(data)
}

data class CustomExceptionData(
    @StringRes val title: Int = R.string.Unknown_Error,
    @StringRes val message: Int = R.string.An_error_occurred_please_try_again_later,
    val code: Int = 202,
    @DrawableRes val image: Int = R.drawable.default_error_icon
)

enum class CustomExceptionCode(val code: Int) {
    NETWORK_EXCEPTION(400),
    THERE_IS_NO_SUITABLE_VARIABLE_EXCEPTION(404),
    DATABASE_EXCEPTION(101),
    UNKNOWN_EXCEPTION(102),
    NOT_VALID_PARAMETERS_EXCEPTION(103),
    AUTHENTICATION_EXCEPTION(401),
    SERVER_EXCEPTION(500),
    UNKNOWN_ERROR(-1),
}
