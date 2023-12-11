package com.example.trovare.ViewModel

import androidx.compose.ui.graphics.ImageBitmap
import com.example.trovare.Data.ConfiguracionDataSource
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.SoporteDatasource
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador
import com.google.android.gms.maps.model.LatLng

//Configuración selecionadas------------------------------------------------------------------------
data class TrovareEstadoConfiguracion(

    val idioma: String = ConfiguracionDataSource.Idiomas[0],
    val unidad: String = ConfiguracionDataSource.Unidades[0],
    val resultoUtil: String = SoporteDatasource.ResultoUtil[0],

)

//Configuración selecionadas------------------------------------------------------------------------
val ubicacionEscom = LatLng(19.504507, -99.147314)

data class TrovareEstadoInicio(

    val categoriaSeleccionada: String = "Restaurantes",
    val lugaresCercanos: MutableList<NearbyPlaces?> = mutableListOf(null),//lista de lugares cercanos
    val lugaresPopulares: MutableList<NearbyPlaces?> = mutableListOf(null),//lista de lugares cercanos
    val lugaresPuntosDeInteres: MutableList<NearbyPlaces?> = mutableListOf(null),//lista de lugares cercanos
    val lugaresCercanosInicializado: Boolean = false,
    val lugaresPopularesInicializado: Boolean = false,
    val lugaresPuntosDeInteresInicializado: Boolean = false,
    val imagenTemporalCategoria: ImageBitmap? = null,
    val imagenTemporalPopulares: ImageBitmap? = null,
    val imagenTemporalPuntosDeInteres: ImageBitmap? = null

)

data class TrovareEstadoMapaPrincipal(

    val origen: LatLng = ubicacionEscom,//origen a marcar en el mapa
    val destino: LatLng = ubicacionEscom,//destino a marcar en el mapa
    val polilineaCod: String = "",//polilinea codificada(como la devuelve la api)
    val polilineaInicializada: Boolean = false,//permite saber si la polilinea se mostrara en el mapa
    val zoom: Float = 15f,//zoom que se hace en el mapa
    val marcadorInicializado: Boolean = false,//permite saber si el marcador se muestra en el mapa
    val marcadores: MutableList<Marcador> = mutableListOf(),
    val marcadoresInicializado: Boolean = false,//permite saber si varios marcadores se muestran en el mapa
    val informacionInicializada: Boolean = false,//permite saber si se despliega la BottomSheet con info del lugar
    val nombreLugar: String = "",
    val ratingLugar: Double? = null,
    val idLugar: String = "",
    val ubicacionLugar: LatLng = ubicacionEscom,
    val imagen: ImageBitmap? = null,
    val distanciaEntrePuntos: Float = 0.0f,//distancia devuelta por la api de rutas
    val tiempoDeViaje: String = "",//tiempo estimado devuelto por la api de rutas

)


data class TrovareEstadoMapaItinerario(

    val origen: LatLng = ubicacionEscom,//origen a marcar en el mapa
    val destino: LatLng = ubicacionEscom,//destino a marcar en el mapa
    val zoom: Float = 15f,//zoom que se hace en el mapa
    val marcadorInicializado: Boolean = false,//permite saber si el marcador se muestra en el mapa
    val marcadores: MutableList<Marcador> = mutableListOf(),
    val marcadoresInicializado: Boolean = false,//permite saber si varios marcadores se muestran en el mapa
    val informacionInicializada: Boolean = false,//permite saber si se despliega la BottomSheet con info del lugar
    val nombreLugar: String = "",
    val ratingLugar: Double? = null,
    val idLugar: String = "",
    val ubicacionLugar: LatLng = ubicacionEscom,
    val imagen: ImageBitmap? = null,
    val distanciaEntrePuntos: Float = 0.0f,//distancia devuelta por la api de rutas
    val tiempoDeViaje: String = "",//tiempo estimado devuelto por la api de rutas

)

data class TrovareEstadoMapaRuta(

    val origenRuta: LatLng = ubicacionEscom,//origen a marcar en el mapa
    val destinoRuta: LatLng = ubicacionEscom,//destino a marcar en el mapa
    val polilineaCodRuta: String = "",
    val polilineaInicializadaRuta: Boolean = false,
    val zoomRuta: Float = 15f,
    val marcadorInicializadoRuta: Boolean = true,
    val nombreLugarRuta: String = "",
    val idLugarRuta: String = "",
    val distanciaEntrePuntos: Float = 0.0f,
    val tiempoDeViaje: String = "",
    val transporteRuta: String = "",

)



