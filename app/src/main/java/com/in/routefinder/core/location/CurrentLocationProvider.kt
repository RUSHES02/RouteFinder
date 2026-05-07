package com.`in`.routefinder.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

interface CurrentLocationProvider {
    suspend fun getCurrentLocation(): LatLng?
}

class CurrentLocationProviderImpl @Inject constructor(
    @param:ApplicationContext
    private val context: Context
) : CurrentLocationProvider {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LatLng? {

        return suspendCancellableCoroutine { continuation ->

            fusedLocationClient
                .lastLocation
                .addOnSuccessListener { location ->

                    val latLng = location?.let {
                        LatLng(it.latitude, it.longitude)
                    }

                    continuation.resume(latLng)
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }
}