package com.example.adkl.breathingbih.model

class BBIHPlace(
        var placeId: Long,
        var lat: Double,
        var lng: Double,
        var name: String,
        var description: String?,
        var type: BBIHPlaceType) {

    fun typeString(): String {
        return when(type) {
            BBIHPlaceType.NO_SMOKE_ZONE -> "No Smoke Zone"
            BBIHPlaceType.NO_SMOKE -> "No Smoke"
        }
    }

}