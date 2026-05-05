package com.`in`.routefinder.data.remote.util

import com.`in`.routefinder.domain.model.RoutePoint

object PolylineUtils {

    fun decode(encoded: String): List<RoutePoint> {
        val polyline = mutableListOf<RoutePoint>()

        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {

            var result = 0
            var shift = 0
            var b: Int

            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlat = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            result = 0
            shift = 0

            do {
                b = encoded[index++].code - 63
                result = result or ((b and 0x1f) shl shift)
                shift += 5
            } while (b >= 0x20)

            val dlng = if ((result and 1) != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            polyline.add(
                RoutePoint(
                    latitude = lat / 1E5,
                    longitude = lng / 1E5
                )
            )
        }

        return polyline
    }
}