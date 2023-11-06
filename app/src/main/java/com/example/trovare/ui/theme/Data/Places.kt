package com.example.trovare.ui.theme.Data

data class LugarAutocompletar(
    val id: String,
    val textoPrimario: String,
    val textoSecundario: String
)

data class Lugar(
    val nombre: String,
    val direccion: String,
    val textoSecundario: String
)