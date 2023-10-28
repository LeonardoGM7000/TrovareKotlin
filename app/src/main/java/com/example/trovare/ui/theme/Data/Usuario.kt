package com.example.trovare.ui.theme.Data

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.trovare.R

data class Usuario(
    val nombre: String,
    @DrawableRes val foto_perfil: Int
)

val usuarioPrueba = Usuario(nombre = "Usuario Prueba", foto_perfil = R.drawable.perfil)