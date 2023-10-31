package com.example.trovare.ui.theme.Data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.SocialDistance
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel

class Configuracion(
    val nombreDeConfig: String,
    val icono: ImageVector,
    val opciones: List<String>,
)

object ConfiguracionDataSource {
    val Idiomas = listOf(
        "Español",
        "Inglés"
    )

    val Unidades = listOf(
        "Km/m",
        "mi/ft"
    )

    val Monedas = listOf(
        "MXN",
        "USD"
    )
}


object SoporteDatasource {
    val ResultoUtil = listOf(
        "",
        "Si",
        "No"
    )
}


val listaDeConfiguracion = listOf(
    Configuracion(nombreDeConfig = "Idioma", icono = Icons.Filled.Language, opciones = ConfiguracionDataSource.Idiomas),
    Configuracion(nombreDeConfig = "Unidades", icono = Icons.Filled.SocialDistance, opciones = ConfiguracionDataSource.Unidades),
    Configuracion(nombreDeConfig = "Moneda", icono = Icons.Filled.AttachMoney, opciones = ConfiguracionDataSource.Monedas)
)