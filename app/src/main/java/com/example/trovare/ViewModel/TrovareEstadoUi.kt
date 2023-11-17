package com.example.trovare.ViewModel

import com.example.trovare.Data.ConfiguracionDataSource
import com.example.trovare.Data.SoporteDatasource


data class TrovareEstadoUi(

//Configuración selecionadas------------------------------------------------------------------------
    val idioma: String = ConfiguracionDataSource.Idiomas[0],
    val unidad: String = ConfiguracionDataSource.Unidades[0],
    val moneda: String = ConfiguracionDataSource.Monedas[0],
    val resultoUtil: String = SoporteDatasource.ResultoUtil[0],
)

