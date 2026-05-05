package com.`in`.routefinder.core.networking

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val type: String,
    val message: String,
    val details: List<ApiErrorDetail>? = null,
    val code: String? = null
)

@Serializable
data class ApiErrorDetail(
    val field: String,
    val message: String
)