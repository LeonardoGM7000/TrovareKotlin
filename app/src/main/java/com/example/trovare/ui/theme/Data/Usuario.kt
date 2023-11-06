package com.example.trovare.ui.theme.Data

import androidx.annotation.DrawableRes
import com.example.trovare.R

data class Usuario(
    val nombre: String,
    @DrawableRes val foto_perfil: Int = R.drawable.perfil,
    val fechaDeRegistro: String,
    val descripcion: String?,
    val lugarDeOrigen: String?,
    val comentarios: List<String>?,
)

val usuarioPrueba = Usuario(nombre = "Usuario Prueba", fechaDeRegistro = "2023", descripcion = "Descripcion del usuario de prueba Descripcion del usuario de prueba",  lugarDeOrigen = "Mexico", comentarios = null)