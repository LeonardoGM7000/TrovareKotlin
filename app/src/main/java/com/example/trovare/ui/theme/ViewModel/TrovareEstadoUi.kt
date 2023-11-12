package com.example.trovare.ui.theme.ViewModel

import android.media.Rating
import com.example.trovare.ui.theme.Data.ConfiguracionDataSource
import com.example.trovare.ui.theme.Data.Lugar
import com.example.trovare.ui.theme.Data.SoporteDatasource


data class TrovareEstadoUi(

//Configuración selecionadas------------------------------------------------------------------------
    val idioma: String = ConfiguracionDataSource.Idiomas[0],
    val unidad: String = ConfiguracionDataSource.Unidades[0],
    val moneda: String = ConfiguracionDataSource.Monedas[0],
    val resultoUtil: String = SoporteDatasource.ResultoUtil[0],
)

data class TrovareEstadoLugar(

    val nombre: String = "p",
    val rating: Double = 1.5,
    val direccion: String = "p",
)
