package com.example.adkl.breathingbih.model

class BBIHPlace {
    var placeId: Long? = 0
    var verifications: Int? = 0
    var lat: Double = 0.0
    var lng: Double = 0.0

    constructor(placeId: Long? = null, verifications: Int? = null, lat: Double, lng: Double) {
        this.placeId = placeId
        this.verifications = verifications
        this.lat = lat
        this.lng = lng
    }

}