package com.`in`.routefinder.data.remote.dto
import kotlinx.serialization.Serializable


@Serializable
data class DirectionsResponseDto(
    val routes: List<RouteDto>
)

@Serializable
data class RouteDto(
    val overview_polyline: PolylineDto,
    val legs: List<LegDto>
)

@Serializable
data class PolylineDto(
    val points: String
)
@Serializable
data class LegDto(
    val distance: DistanceDto,
    val duration: DurationDto
)

@Serializable
data class DistanceDto(
    val text: String
)

@Serializable
data class DurationDto(
    val text: String
)