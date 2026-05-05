package com.`in`.routefinder.data.remote.dto
import kotlinx.serialization.Serializable


@Serializable
data class DirectionsResponseDto(
    val routes: List<RouteDto>
)

@Serializable
data class RouteDto(
    val overview_polyline: PolylineDto
)

@Serializable
data class PolylineDto(
    val points: String
)