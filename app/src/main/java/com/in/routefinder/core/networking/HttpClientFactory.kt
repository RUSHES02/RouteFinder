package com.`in`.routefinder.core.networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

/**
 * A factory object responsible for creating and configuring HTTP clients.
 *
 * This object provides a centralized way to create `HttpClient` instances
 * with predefined configurations, such as content negotiation, logging,
 * timeouts, and authentication. It also handles token refreshing using a
 * `TokenManager`.
 */
object HttpClientFactory {
	private val mutex = Mutex()
    private const val EMULATOR_HOST = "10.0.2.2"
    private val emulatorPorts = setOf(
        8001,
        8080
    )
    private val publicEndpoints = setOf(
        "/auth/login",
        "/auth/signup",
        "/ping"
    )
	/**
	 * Creates and configures an [HttpClient] for making API requests, including token refresh handling.
	 *
	 * This function sets up two HTTP clients:
	 * 1. **refreshClient:** Used specifically for refreshing access tokens.
	 * 2. **client:** The main HTTP client used for general API requests.
	 *
	 * The `client` is configured with:
	 *   - Logging: Logs all requests and responses.
	 *   - Content Negotiation: Handles JSON serialization and deserialization.
	 *   - Authentication: Uses Bearer token authentication with automatic token refresh.
	 *   - Default Request Settings: Sets the default content type to `application/json`.
	 *
	 * The `refreshClient` is configured with:
	 *    - Content Negotiation: Handles JSON serialization and deserialization.
	 *    - Timeout: Sets connect, request and socket timeouts to 60 seconds.
	 *
	 * **Token Refresh Logic:**
	 *   - The `refreshTokens` block within the `Auth` plugin handles the token refresh process.
	 *   - It first checks if a refresh token is available. If not, it clears the stored tokens and throws an exception.
	 *   - It makes a `POST` request to the `/auth/refresh` endpoint with the refresh token.
	 *   - If the request is successful, it updates the stored access and refresh tokens.
	 *   - If the request fails or an exception occurs, it clears the stored tokens and throws an exception.
	 *   - It uses a `Mutex` to ensure that only one token refresh request is active
	 */
    fun create(
        engine: HttpClientEngine,
        isDebug: Boolean
    ): HttpClient {

        val jsonConfig = Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
            prettyPrint = isDebug
        }

        val refreshClient = HttpClient(engine) {
            install(ContentNegotiation) { json(jsonConfig) }
            install(HttpTimeout) {
                connectTimeoutMillis = 60.seconds.inWholeMilliseconds
                requestTimeoutMillis = 60.seconds.inWholeMilliseconds
                socketTimeoutMillis = 60.seconds.inWholeMilliseconds
            }
        }

        return HttpClient(engine) {

            install(ContentNegotiation) { json(jsonConfig) }

            if (isDebug) {
                install(Logging) {
                    logger = Logger.ANDROID
                    level = LogLevel.BODY
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }

//            install(Auth) {
//                bearer {
//
//                    loadTokens {
//                        val access = tokenManager.getAccessTokenOnce()
//                        val refresh = tokenManager.getRefreshTokenOnce()
//                        if (access != null && refresh != null) {
//                            BearerTokens(access, refresh)
//                        } else null
//                    }
//
//                    refreshTokens {
//                        mutex.withLock {
//                            val refresh = tokenManager.getRefreshTokenOnce()
//                                ?: return@refreshTokens null
//
//                            val result = safeCall<RefreshTokenResponse> {
//                                refreshClient.post(constructUrl("/auth/refresh")) {
//                                    setBody(RefreshTokenRequest(refresh))
//                                }
//                            }
//
//                            when (result) {
//                                is Response.Success -> {
//                                    tokenManager.saveAccessTokens(
//                                        result.data.data.accessToken,
//                                        result.data.data.refreshToken
//                                    )
//                                    BearerTokens(
//                                        result.data.data.accessToken,
//                                        result.data.data.refreshToken
//                                    )
//                                }
//                                else -> null // triggers logout / unauthorized flow
//                            }
//                        }
//                    }
//
//                    sendWithoutRequest { request ->
//                        val path = request.url.encodedPath
//                        val host = request.url.host
//                        val port = request.url.port
//
//                        val isPublicEndpoint = path in publicEndpoints
//                        val isEmulatorHost = host == EMULATOR_HOST && port in emulatorPorts
//
//                        isPublicEndpoint && isEmulatorHost
//                    }
//                }
//            }
        }
    }
}