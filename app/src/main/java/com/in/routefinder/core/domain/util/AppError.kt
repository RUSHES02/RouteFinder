package com.`in`.routefinder.core.domain.util

import com.`in`.routefinder.core.networking.ApiErrorDetail


sealed interface AppError: MyError {

    val isRetryable: Boolean
    val uiMessage: UiMessage?

    // Connectivity
    data object NoInternet : AppError {
        override val isRetryable = true
        override val uiMessage = UiMessage.Text("No internet connection")
    }

    // Auth
    data object Unauthorized : AppError {
        override val isRetryable = false
        override val uiMessage = UiMessage.Text("Session expired. Please login again")
    }

    data object Forbidden : AppError {
        override val isRetryable = false
        override val uiMessage = UiMessage.Text("Access denied")
    }

    // Resource
    data object NotFound : AppError {
        override val isRetryable = false
        override val uiMessage = UiMessage.Text("Resource not found")
    }

    // Validation (form errors, etc.)
    data class Validation(
        val details: List<ApiErrorDetail>? = null
    ) : AppError {
        override val isRetryable = false
        override val uiMessage = UiMessage.Text("Invalid input")
    }

    // Server / backend
    data class Server(
        val code: String? = null,
        val message: String? = null
    ) : AppError {
        override val isRetryable = true
        override val uiMessage =
            message?.let { UiMessage.Text(it) }
                ?: UiMessage.Text("Something went wrong")
    }

    // Data layer issues
    data object Serialization : AppError {
        override val isRetryable = false
        override val uiMessage = UiMessage.Text("Data error occurred")
    }

    // Fallback
    data object Unknown : AppError {
        override val isRetryable = true
        override val uiMessage = UiMessage.Text("Something went wrong")
    }
}

sealed interface UiMessage {
    data class Text(val value: String) : UiMessage
    data class Res(val resId: Int) : UiMessage
}