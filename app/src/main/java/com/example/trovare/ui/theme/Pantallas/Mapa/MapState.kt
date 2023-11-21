package com.example.trovare.ui.theme.Pantallas.Mapa

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.time.Duration

data class MapState(
    val lastKnownLocation: Location?,
)

data class Marcador(
    val ubicacion: LatLng,
    val id: String
)

data class RutaInfo(
    val distancia: Float,
    val duracion: String,
    val polilinea: String
)
