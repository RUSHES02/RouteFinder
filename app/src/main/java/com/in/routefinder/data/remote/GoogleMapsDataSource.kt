package com.`in`.routefinder.data.remote

import com.`in`.routefinder.core.domain.util.Response
import com.`in`.routefinder.core.networking.HttpClientFactory
import com.`in`.routefinder.core.networking.NetworkError
import com.`in`.routefinder.core.networking.constructUrl
import com.`in`.routefinder.core.networking.safeCall
import com.`in`.routefinder.data.remote.dto.DirectionsResponseDto
import com.`in`.routefinder.data.remote.dto.PlaceDetailsResponseDto
import com.`in`.routefinder.data.remote.dto.PlacesAutocompleteResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class GoogleMapsDataSource(
    private val client: HttpClient,
    private val apiKey: String
)  {

    suspend fun getAutocomplete(
        query: String
    ): Response<PlacesAutocompleteResponseDto, NetworkError> {

        val encoded = withContext(Dispatchers.IO) {
            URLEncoder.encode(query, "UTF-8")
        }

        return safeCall {
            client.get(
                constructUrl("/maps/api/place/autocomplete/json")
            ) {
                parameter("input", encoded)
                parameter("key", apiKey)
            }
        }
    }

    suspend fun getPlaceDetails(
        placeId: String
    ): Response<PlaceDetailsResponseDto, NetworkError> {

        return safeCall {
            client.get(
                constructUrl("/maps/api/place/details/json")
            ) {
                parameter("place_id", placeId)
                parameter("key", apiKey)
            }
        }
    }

    suspend fun getDirections(
        origin: String,
        destination: String
    ): Response<DirectionsResponseDto, NetworkError> {

        return safeCall {
            client.get(
                constructUrl("/maps/api/directions/json")
            ) {
                parameter("origin", origin)
                parameter("destination", destination)
                parameter("key", apiKey)
            }
        }
    }
}