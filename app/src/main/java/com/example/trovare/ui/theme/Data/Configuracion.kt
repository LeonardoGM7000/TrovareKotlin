package com.example.trovare.ui.theme.Data

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Language
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.trovare.R
import com.example.trovare.ui.theme.Pantallas.Configuracion

data class Configuracion(
    val nombreDeConfig: String,
    val icono: ImageVector,
    val estadoActualConfig: String,
)

val listaDeConfiguracion = listOf(
    Configuracion(nombreDeConfig = "Idioma", icono = Icons.Filled.Language, estadoActualConfig = "Español")
)