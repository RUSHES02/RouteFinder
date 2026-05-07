package com.`in`.routefinder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlacesAutocompleteResponseDto(
    val predictions: List<PredictionDto>
)

@Serializable
data class PredictionDto(
    val place_id: String,
    val description: String
)