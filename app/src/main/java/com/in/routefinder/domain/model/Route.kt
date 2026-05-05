package com.`in`.routefinder.domain.model

data class Route(
    val points: List<RoutePoint>
)

data class RoutePoint(
    val latitude: Double,
    val longitude: Double
)