package com.example.trovare.Data

import androidx.annotation.DrawableRes

data class Itinerario(
    val nombre: String,
    @DrawableRes val imagen: Int?,
    val actividades: List<Actividad>?
)

data class Actividad(
    val nombre: String
)
