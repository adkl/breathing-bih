package com.example.adkl.breathingbih.service

import com.example.adkl.breathingbih.model.BBIHPlace

class NonSmokingPlacesService {
    companion object {
        fun getNonSmokingPlaces(): Array<BBIHPlace> {
            return arrayOf(
                    BBIHPlace(lat = 43.85343251573264, lng = 18.371812105178833),
                    BBIHPlace(lat = 43.85873773452042, lng = 18.423046320676804),
                    BBIHPlace(lat = 43.84695459628817, lng = 18.35528500378132))
        }
    }
}