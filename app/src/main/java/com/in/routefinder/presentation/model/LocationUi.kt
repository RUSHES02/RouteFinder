package com.`in`.routefinder.presentation.model

data class LocationUi(
    val id: String,
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val isCurrentLocation: Boolean = false
)