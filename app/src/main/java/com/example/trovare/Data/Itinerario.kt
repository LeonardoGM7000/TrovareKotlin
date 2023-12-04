package com.example.trovare.Data

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

data class Itinerario(
    var nombre: String,
    var autor: String,
    var lugares: MutableList<Lugar>?
)

data class Lugar(
    val id: String,
    val nombreLugar: String,
    var fechaDeVisita: LocalDate?,
    var horaDeVisita: Hora?,
    var ubicacion: LatLng?,
    var imagen: ImageBitmap?
)

data class Hora(
    val hora: Int,
    val minuto: Int,
)

val itinerarioPrueba: Itinerario = Itinerario(
    nombre = "Itinerario",
    autor = "",
    lugares = null
)

data class Actividad(
    val nombre: String
)
