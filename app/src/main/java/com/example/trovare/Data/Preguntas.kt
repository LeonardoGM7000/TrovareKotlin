package com.example.trovare.Data

import androidx.annotation.StringRes
import com.example.trovare.R


data class Pregunta(

    val pregunta: String = "",
    val respuesta: String = "",
    val id: String = ""
)


/*
data class Pregunta(
    @StringRes val pregunta: Int,
    @StringRes val respuesta: Int
)


val listaDePreguntas = listOf(
    Pregunta(R.string.pregunta1, R.string.respuesta1),
    Pregunta(R.string.pregunta1, R.string.respuesta1),
    Pregunta(R.string.pregunta1, R.string.respuesta1),
    Pregunta(R.string.pregunta1, R.string.respuesta1),
)

 */