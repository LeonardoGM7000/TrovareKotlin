package com.example.trovare.Data

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class Itinerario(
    var nombre: String,
    var autor: String,
    //@DrawableRes val imagen: Int?,
    var fechas: List<LocalDate>?
)



/*
data class Fecha(
    var fecha: LocalDate,
    val actividades: List<Actividad>?
)

 */

data class Actividad(
    val nombre: String
)

val itinerarioPrueba: Itinerario = Itinerario(
    nombre = "Itinerario 1",
    autor = "",
    fechas = null
)
