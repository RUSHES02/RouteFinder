package com.`in`.routefinder.domain

import com.`in`.routefinder.core.domain.error.AppError
import com.`in`.routefinder.core.domain.result.Response
import com.`in`.routefinder.domain.model.Location
import com.`in`.routefinder.domain.model.Route

interface MapsRepository {

    suspend fun searchLocations(
        query: String
    ): Response<List<Location>, AppError>

    suspend fun getLocationDetails(
        placeId: String,
        name: String
    ): Response<Location, AppError>

    suspend fun getRoute(
        start: Location,
        destination: Location
    ): Response<Route, AppError>
}