package com.example.adkl.breathingbih.service

import com.example.adkl.breathingbih.model.BBIHPlace
import com.example.adkl.breathingbih.model.BBIHPlaceType

class NonSmokingPlacesService {
    companion object {
        fun getNonSmokingPlaces(): Array<BBIHPlace> {
            // TODO Call external service
            // TODO Later add dependency to Locations API
            return arrayOf(
                    BBIHPlace(lat = 43.85343251573264,
                            lng = 18.371812105178833,
                            name = "Mrvica",
                            placeId = 1,
                            type = BBIHPlaceType.NO_SMOKE_ZONE,
                            description = null),
                    BBIHPlace(lat = 43.85873773452042,
                            lng = 18.423046320676804,
                            name = "Klopa",
                            placeId = 2,
                            type = BBIHPlaceType.NO_SMOKE,
                            description = null),
                    BBIHPlace(lat = 43.84695459628817,
                            lng = 18.35528500378132,
                            name = "KUP Caffee",
                            placeId = 3,
                            type = BBIHPlaceType.NO_SMOKE_ZONE,
                            description = null))
        }
    }
}