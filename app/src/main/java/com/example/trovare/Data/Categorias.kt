package com.example.trovare.Data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Attractions
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Hotel
import androidx.compose.material.icons.rounded.Museum
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.ui.graphics.vector.ImageVector

data class Categoria(
    val nombre: String,
    val icono: ImageVector,
)

val categorias = listOf(
    Categoria(nombre = "Restaurantes", icono = Icons.Rounded.Restaurant),
    Categoria(nombre = "Museos", icono = Icons.Rounded.Museum),
    Categoria(nombre = "Parques", icono = Icons.Rounded.Park),
    Categoria(nombre = "Hoteles", icono = Icons.Rounded.Hotel),
)