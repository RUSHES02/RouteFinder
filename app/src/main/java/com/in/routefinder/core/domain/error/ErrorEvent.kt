package com.`in`.routefinder.core.domain.error

import com.`in`.routefinder.core.networking.NetworkError

/**
 * Represents an event indicating an error that occurred during a network operation or other process.
 *
 * This sealed interface provides a type-safe way to handle different types of errors.
 */
sealed interface ErrorEvent {
	data class Error(val error: NetworkError) : ErrorEvent
}