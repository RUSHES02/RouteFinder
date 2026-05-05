package com.`in`.routefinder.data.remote.mappers

import com.`in`.routefinder.data.remote.dto.DirectionsResponseDto
import com.`in`.routefinder.data.remote.dto.PlaceDetailsResponseDto
import com.`in`.routefinder.data.remote.util.PolylineUtils
import com.`in`.routefinder.domain.model.Location
import com.`in`.routefinder.domain.model.Route
import com.`in`.routefinder.domain.model.RoutePoint

fun PlaceDetailsResponseDto.toDomain(placeId: String, name: String): Location {
    return Location(
        id = placeId,
        name = name,
        address = name,
        latitude = result.geometry.location.lat,
        longitude = result.geometry.location.lng
    )
}

fun DirectionsResponseDto.toDomain(): Route {
    val polyline = routes.firstOrNull()?.overview_polyline?.points ?: ""

    return Route(
        points = PolylineUtils.decode(polyline)
    )
}