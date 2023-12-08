package com.example.trovare.Data

import androidx.compose.ui.graphics.ImageBitmap
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalTime

data class Itinerario(
    var nombre: String,
    var autor: String,
    var lugares: MutableList<Lugar>?
)

data class Lugar(
    val id: String,
    val nombreLugar: String,
    //Fehcas
    var fechaDeVisita: LocalDate?,
    var horaDeVisita: LocalTime?,
    //Rutas
    var origen: LatLng?,
    var ubicacion: LatLng?,
    var ruta: String?,
    var zoom: Float,
    var transporte: String = "",
    //
    var imagen: ImageBitmap?
)


val itinerarioPrueba: Itinerario = Itinerario(
    nombre = "Itinerario",
    autor = "",
    lugares = null
)

