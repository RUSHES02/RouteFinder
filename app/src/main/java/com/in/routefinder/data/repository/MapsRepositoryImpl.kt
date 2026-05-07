package com.`in`.routefinder.data.repository

import com.`in`.routefinder.core.domain.error.AppError
import com.`in`.routefinder.core.domain.result.Response
import com.`in`.routefinder.core.domain.result.map
import com.`in`.routefinder.core.domain.result.mapToAppError
import com.`in`.routefinder.core.networking.toAppError
import com.`in`.routefinder.data.remote.GoogleMapsDataSource
import com.`in`.routefinder.data.remote.util.PolylineUtils
import com.`in`.routefinder.domain.MapsRepository
import com.`in`.routefinder.domain.model.Location
import com.`in`.routefinder.domain.model.Route
import com.`in`.routefinder.domain.model.RouteInfo
import com.`in`.routefinder.domain.model.RoutePoint

class MapsRepositoryImpl(
    private val dataSource: GoogleMapsDataSource
) : MapsRepository {

    override suspend fun searchLocations(query: String): Response<List<Location>, AppError> {

        if (query.isBlank()) {
            return Response.Success(emptyList())
        }

        return dataSource.getAutocomplete(query)
            .map { dto ->
                dto.predictions.map { prediction ->
                    Location(
                        id = prediction.place_id,
                        name = prediction.description,
                        address = prediction.description,
                        latitude = 0.0,
                        longitude = 0.0
                    )
                }
            }
            .mapToAppError { it.toAppError() }
    }

    override suspend fun getLocationDetails(placeId: String, name: String): Response<Location, AppError> {

        return dataSource.getPlaceDetails(placeId)
            .map { dto ->
                val loc = dto.result.geometry.location
                Location(
                    id = placeId,
                    name = name,
                    address = name,
                    latitude = loc.lat,
                    longitude = loc.lng
                )
            }
            .mapToAppError { it.toAppError() }
    }

    override suspend fun getRoute(start: Location, destination: Location): Response<Route, AppError> {

        val origin = "${start.latitude},${start.longitude}"
        val dest = "${destination.latitude},${destination.longitude}"

        return dataSource.getDirections(origin, dest)
            .map { dto ->
                val route = dto.routes.firstOrNull()
                val decoded = PolylineUtils.decode(route?.overview_polyline?.points.orEmpty())
                val leg = route?.legs?.firstOrNull()

                Route(
                    points = decoded.map {
                        RoutePoint(
                            latitude = it.latitude,
                            longitude = it.longitude
                        )
                    },
                    routeInfo = leg?.let {
                        RouteInfo(
                            distanceText = it.distance.text,
                            durationText = it.duration.text
                        )
                    }
                )
            }
            .mapToAppError { it.toAppError() }
    }
}