package com.example.trovare.Data

import androidx.annotation.DrawableRes

data class Itinerario(
    var nombre: String,
    var autor: String,
    //@DrawableRes val imagen: Int?,
    val actividades: List<Actividad>?
)

data class Actividad(
    val nombre: String
)

val itinerarioPrueba: Itinerario = Itinerario(
    nombre = "Itinerario 1",
    autor = "",
    actividades = null
)
