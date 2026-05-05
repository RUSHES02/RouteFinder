package com.`in`.routefinder.domain

import com.`in`.routefinder.domain.model.Location
import com.`in`.routefinder.domain.model.Route

interface LocationRepository {
    suspend fun searchLocations(query: String): List<Location>
    suspend fun getRoute(
        start: Location,
        destination: Location
    ): Route
}