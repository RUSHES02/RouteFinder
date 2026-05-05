package com.`in`.routefinder.core.networking

import com.`in`.routefinder.core.domain.util.AppError
import com.`in`.routefinder.core.domain.util.AppError.Forbidden
import com.`in`.routefinder.core.domain.util.AppError.NoInternet
import com.`in`.routefinder.core.domain.util.AppError.NotFound
import com.`in`.routefinder.core.domain.util.AppError.Serialization
import com.`in`.routefinder.core.domain.util.AppError.Server
import com.`in`.routefinder.core.domain.util.AppError.Unauthorized
import com.`in`.routefinder.core.domain.util.AppError.Unknown
import com.`in`.routefinder.core.domain.util.MyError

sealed class NetworkError: MyError {

    data object NoInternet : NetworkError()

    data object Serialization : NetworkError()

    data class HttpError(
        val statusCode: Int,
        val message: String?, // backend message
        val details: List<ApiErrorDetail>? = null,
        val code: String? = null // backend-specific code
    ) : NetworkError()

    data class Unknown(
        val throwable: Throwable? = null
    ) : NetworkError()
}

fun NetworkError.toAppError(): AppError {
    return when (this) {

        NetworkError.NoInternet ->
            NoInternet

        NetworkError.Serialization ->
            Serialization

        is NetworkError.HttpError -> {
            when (statusCode) {

                400, 422 ->
                    AppError.Validation(details)

                401 ->
                    Unauthorized

                403 ->
                    Forbidden

                404 ->
                    NotFound

                in 500..599 ->
                    Server(code = code, message = message)

                else ->
                    Server(code = code, message = message)
            }
        }

        is NetworkError.Unknown ->
            Unknown
    }
}