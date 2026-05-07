package com.`in`.routefinder.core.networking

import com.`in`.routefinder.core.domain.result.Response
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Converts an [HttpResponse] to a [Response] object, handling success and error scenarios.
 *
 * This function is designed to process responses from network requests and map them to a `Result`
 * type, which can either be a successful `Result.Success` containing the parsed data, or a
 * `Result.Error` containing a `NetworkError` describing the failure.
 *
 * **Success Scenarios (200-299):**
 *   - If the response status code is within the 200-299 range (indicating success):
 *     - **No Content (204):** If the status is `HttpStatusCode.NoContent`, it's treated as a successful
 *       operation with no content. In this case, `Result.Success(Unit as T)` is returned, assuming `T` is `Unit`.
 *     - **Content Present:** Otherwise, the function attempts to deserialize the response body to type `T`.
 *       - If successful, `Result.Success(data)` is returned.
 *       - If a `SerializationException` occurs during deserialization, it's considered a serialization error,
 *         and `Result.Error(NetworkError.Serialization)` is returned.
 *
 * **Error Scenarios (400-599):**
 *   - If the response status code is within the 400-599 range (indicating a client or server error):
 *     - The function attempts to parse the error response body as an `ApiErrorResponse`.
 *       - If parsing fails (due to `SerializationException`), a `null` `errorBody` is used, and a generic error message is used.
 *     - A `NetworkError` is constructed based on the HTTP status code:
 *       - `HttpStatusCode.BadRequest`: `NetworkError.BadRequest`
 *       - `HttpStatusCode.Unauthorized`: `NetworkError.Unauthorized`
 *       - `HttpStatusCode.Forbidden`: `NetworkError.Forbidden`
 *       - `HttpStatusCode.NotFound`: `NetworkError.NotFound`
 *       - `HttpStatusCode.UnprocessableEntity`: `NetworkError.UnprocessableEntity`
 *       - Other 4xx-
 */
suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Response<T, NetworkError> {

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    return when (response.status.value) {

        in 200..299 -> {
            if (response.status == HttpStatusCode.NoContent) {
                @Suppress("UNCHECKED_CAST")
                Response.Success(Unit as T)
            } else {
                val data: T = try {
                    response.body()
                } catch (e: SerializationException) {
                    return Response.Error(NetworkError.Serialization)
                }
                Response.Success(data)
            }
        }

        in 400..599 -> {

            val statusCode = response.status.value

            // Safely read body (may be null)
            val rawBody: String? = runCatching {
                response.bodyAsText()
            }.getOrNull()?.takeIf { it.isNotBlank() }

            // Parse only if looks like JSON
            val parsed: ApiErrorResponse? =
                rawBody?.takeIf { it.startsWith("{") }
                    ?.let {
                        runCatching {
                            json.decodeFromString<ApiErrorResponse>(it)
                        }.getOrNull()
                    }

            Response.Error(
                NetworkError.HttpError(
                    statusCode = statusCode,
                    message = parsed?.message ?: rawBody, // may still be null → OK
                    details = parsed?.details,
                    code = parsed?.code
                )
            )
        }

        else -> Response.Error(NetworkError.Unknown())
    }
}