package com.example.trovare.ui.theme.Data

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.trovare.R

//Categorías

//Explorar más sitios
data class Explorar(
    val Titulo: String,
    val lugar: String,
    val Imagen: String,
)

val listaDeExplorar = listOf(
    Explorar("Populares", lugar = "Hotel", "https://cdn.forbes.com.mx/2020/07/hoteles-Grand-Velas-Resorts-e1596047698604.jpg"),
    Explorar("Para ti", lugar = "Restaurante", "https://static.wixstatic.com/media/5cecee_78de0f63516c41f99bd9fe3b71dd2b13~mv2.jpg/v1/fill/w_560,h_572,al_c,q_80,usm_0.66_1.00_0.01,enc_auto/Image-empty-state.jpg"),
    Explorar("Mejor puntuado", lugar = "Playa", "https://upload.wikimedia.org/wikipedia/commons/9/96/Barbados_beach.jpg"),


)

