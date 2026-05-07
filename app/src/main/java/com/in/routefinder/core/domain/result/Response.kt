package com.`in`.routefinder.core.domain.result

import com.`in`.routefinder.core.domain.error.AppError
import com.`in`.routefinder.core.domain.error.MyError
import com.`in`.routefinder.core.networking.NetworkError


// Type alias to represent domain-specific errors.
typealias DomainError = MyError

/**
 * A sealed interface representing a generic result with either:
 * - `Success(data: D)`: Contains the successful result.
 * - `Error(error: E)`: Contains an error of type `E` (must extend `Error`).
 */
sealed interface Response<out D, out E: MyError> {
    data class Success<out D>(val data: D): Response<D, Nothing>
    data class Error<out E: DomainError>(val error: E): Response<Nothing, E>
}

/**
 * Maps a successful result (`Success<D>`) to a new result of type `Result<R, E>`.
 * If the result is an error, it remains unchanged.
 */
inline fun <T, E: MyError, R> Response<T, E>.map(map: (T) -> R): Response<R, E> {
    return when (this) {
        is Response.Error -> Response.Error(error)  // Pass through error unchanged.
        is Response.Success -> Response.Success(map(data))  // Apply mapping function to success data.
    }
}

/**
 * Converts a successful result (`Success<T>`) into an `EmptyResult<E>` (Success<Unit>).
 * If the result is an error, it remains unchanged.
 */
fun <T, E: MyError> Response<T, E>.asEmptyDataResult(): EmptyResult<E> {
    return map { Unit }  // Converts success value to `Unit`
}

/**
 * Executes the given action if the result is a `Success<T>`.
 * Returns the original result unchanged.
 */
inline fun <T, E: MyError> Response<T, E>.onSuccess(action: (T) -> Unit): Response<T, E> {
    return when (this) {
        is Response.Error -> this  // Return unchanged if an error.
        is Response.Success -> {
            action(data)  // Execute action with success data.
            this  // Return unchanged.
        }
    }
}

/**
 * Executes the given action if the result is an `Error<E>`.
 * Returns the original result unchanged.
 */
inline fun <T, E: MyError> Response<T, E>.onError(action: (E) -> Unit): Response<T, E> {
    return when (this) {
        is Response.Error -> {
            action(error)  // Execute action with error data.
            this  // Return unchanged.
        }
        is Response.Success -> this  // Return unchanged if success.
    }
}

inline fun <T> Response<T, NetworkError>.mapToAppError(
    mapper: (NetworkError) -> AppError
): Response<T, AppError> =
    when (this) {
        is Response.Success -> Response.Success(data)
        is Response.Error -> Response.Error(mapper(error))
    }

// Type alias for results that return no data (`Unit`) but may contain an error.
typealias EmptyResult<E> = Response<Unit, E>