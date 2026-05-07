package com.`in`.routefinder.core.networking

import com.`in`.routefinder.core.domain.result.Response
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Response<T, NetworkError> {

    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return Response.Error(NetworkError.NoInternet)

    } catch (e: SerializationException) {
        return Response.Error(NetworkError.Serialization)

    } catch (e: Exception) {
        currentCoroutineContext().ensureActive()
        return Response.Error(NetworkError.Unknown(e))
    }

    return responseToResult(response)
}