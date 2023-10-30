package com.example.trovare.ui.theme.Navegacion

import com.example.trovare.ui.theme.Data.ConfiguracionDataSource


data class ConfiguracionEstadoUi(

//Configuración selecionadas------------------------------------------------------------------------
    val idioma: String = ConfiguracionDataSource.Idiomas[0],
    val unidad: String = ConfiguracionDataSource.Unidades[0],
    val moneda: String = ConfiguracionDataSource.Monedas[0],

)
