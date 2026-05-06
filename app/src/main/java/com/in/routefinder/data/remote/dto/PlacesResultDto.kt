package com.`in`.routefinder.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaceDetailsResponseDto(
    val result: PlaceResultDto
)

@Serializable
data class PlaceResultDto(
    val geometry: GeometryDto
)

@Serializable
data class GeometryDto(
    val location: LatLngDto
)

@Serializable
data class LatLngDto(
    val lat: Double,
    val lng: Double
)