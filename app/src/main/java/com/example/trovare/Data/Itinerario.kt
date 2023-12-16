package com.example.trovare.Data

import androidx.compose.ui.graphics.ImageBitmap
import java.time.LocalDate

data class Itinerario(
    var id: String?,
    var nombre: String,
    var autor: String,
    var lugares: MutableList<Lugar>?,

    // Creamos variable para guardar imagenes
    var imagen: String?
)

data class Lugar(
    val id: String,
    val nombreLugar: String,
    var fechaDeVisita: String?,
    var horaDeVisita: String?,
    var imagen: String?
)

data class Hora(
    val hora: Int,
    val minuto: Int,
)

val itinerarioPrueba: Itinerario = Itinerario(
    id = "",
    nombre = "Itinerario",
    autor = "",
    lugares = null,
    imagen = null
)

data class Actividad(
    val nombre: String
)
