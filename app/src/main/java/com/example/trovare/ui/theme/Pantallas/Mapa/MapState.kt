package com.example.trovare.ui.theme.Pantallas.Mapa

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class MapState(
    val lastKnownLocation: Location?,
)

data class Marcador(
    val ubicacion: LatLng,
    val id: String
)
