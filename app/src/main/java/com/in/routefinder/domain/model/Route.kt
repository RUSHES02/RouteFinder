package com.`in`.routefinder.domain.model

data class Route(
    val points: List<RoutePoint>,
    val routeInfo: RouteInfo? = null
)

data class RoutePoint(
    val latitude: Double,
    val longitude: Double,
)

data class RouteInfo(
    val distanceText: String,
    val durationText: String
)