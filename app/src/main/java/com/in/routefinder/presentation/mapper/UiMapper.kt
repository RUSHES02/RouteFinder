package com.`in`.routefinder.presentation.mapper

import com.`in`.routefinder.domain.model.Location
import com.`in`.routefinder.domain.model.RouteInfo
import com.`in`.routefinder.presentation.model.LocationUi
import com.`in`.routefinder.presentation.model.RouteInfoUi

fun Location.toUi(): LocationUi {
    return LocationUi(
        id = id,
        name = name,
        address = address,
        lat = latitude,
        lng = longitude
    )
}

fun LocationUi.toDomain(): Location {
    return Location(
        id = id,
        name = name,
        address = address,
        latitude = lat,
        longitude = lng
    )
}

fun RouteInfo.toUi(): RouteInfoUi {
    return RouteInfoUi(
        distanceText = distanceText,
        durationText = durationText
    )
}