package com.example.trovare.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trovare.Data.Itinerario
import com.example.trovare.Data.Lugar
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.itinerarioPrueba
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime

/**
 * [TrovareViewModel] guarda información de la aplicación dentro del ciclo de vida.
 */

class TrovareViewModel : ViewModel() {


    //--------------------------------------------------------------------------------------------//
    //-------------------------------------IMAGENES-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //Manejo de imagenes----------------------------------------------------------------------------
    private val _imagen = mutableStateOf<ImageBitmap?>(null)
    val imagen: State<ImageBitmap?> = _imagen

    private val _imagenes = mutableStateOf(mutableListOf<ImageBitmap?>())
    val imagenes: State<MutableList<ImageBitmap?>?> = _imagenes

    fun reiniciarImagen() {
        _imagen.value = null
    }

    //--------------------------------------------------------------------------------------------//
    //------------------------------ESTADO DE LA UI-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //guardar variables de estado de la UI----------------------------------------------------------

    private val _estadoUi = MutableStateFlow(TrovareEstadoConfiguracion())
    val uiState: StateFlow<TrovareEstadoConfiguracion> = _estadoUi.asStateFlow()

    fun setIdioma(nuevoIdioma: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                idioma = nuevoIdioma,
            )
        }
    }

    fun setUnidades(nuevaUnidad: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                unidad = nuevaUnidad,
            )
        }
    }

    fun setMonedas(nuevaMoneda: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                moneda = nuevaMoneda,
            )
        }
    }

    fun setResultoUtil(nuevaSeleccion: String){
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                resultoUtil = nuevaSeleccion
            )
        }
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------------------------UBICACIÓN----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //Ubicacion-------------------------------------------------------------------------------------

    private val _ubicacionActual = MutableStateFlow(LatLng(19.504507, -99.147314))
    val ubicacionActual = _ubicacionActual.asStateFlow()

    fun setUbicacionActual(nuevoValor: LatLng) {
        _ubicacionActual.value = nuevoValor
    }

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
        )
    )
    //Obtener ubicacion en tiempo real
    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }
        } catch (e: SecurityException) {
            //TODO
        }
    }
    //Obtener ubicacion en temporal en LatLng y cargarla al viewmodel
    fun getLastLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ){
        try {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        // Aquí tienes la latitud y longitud.
                        // Puedes usar estas variables en tu lógica o pasárselas al ViewModel según sea necesario.
                        setUbicacionActual(LatLng(latitude, longitude))
                    }
                }
                .addOnFailureListener { exception ->
                    //TODO
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    //--------------------------------------------------------------------------------------------//
    //---------------------------------MAPA PRINCIPAL---------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //MAPA PRINCIPAL -------------------------------------------------------------------------------

    private val _estadoMapa = MutableStateFlow(TrovareEstadoMapaPrincipal())
    val estadoMapa: StateFlow<TrovareEstadoMapaPrincipal> = _estadoMapa.asStateFlow()

    fun setOrigen(nuevoValor: LatLng) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                origen = nuevoValor,
            )
        }
    }

    fun setDestino(nuevoValor: LatLng) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                destino = nuevoValor,
            )
        }
    }

    fun setPolilineaCod(nuevoValor: String) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                polilineaCod = nuevoValor,
            )
        }
    }

    fun setPolilineaInicializada(nuevoValor: Boolean) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                polilineaInicializada = nuevoValor,
            )
        }
    }

    fun setZoom(nuevoValor: Float) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                zoom = nuevoValor,
            )
        }
    }

    fun setMarcadorInicializado(nuevoValor: Boolean) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                marcadorInicializado = nuevoValor,
            )
        }
    }

    fun setMarcadoresInicializado(nuevoValor: Boolean) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                marcadoresInicializado = nuevoValor,
            )
        }
    }

    fun setInformacionInicializada(nuevoValor: Boolean) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                informacionInicializada = nuevoValor,
            )
        }
    }

    fun setNombreLugar(nuevoValor: String) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                nombreLugar = nuevoValor,
            )
        }
    }

    fun setRatingLugar(nuevoValor: Double) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                ratingLugar = nuevoValor,
            )
        }
    }

    fun setIdLugar(nuevoValor: String) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                idLugar = nuevoValor,
            )
        }
    }

    fun setUbicacionLugar(nuevoValor: LatLng) {
        _estadoMapa.update { estadoActual ->
            estadoActual.copy(
                ubicacionLugar = nuevoValor,
            )
        }
    }

    //--------------------------------------------------------------------------------------------//
    //------------------------------MAPA DE RUTAS ITINERARIO--------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //MAPA SELECCION DE RUTAS EN ITINERARIO---------------------------------------------------------

    private val _estadoMapaRuta = MutableStateFlow(TrovareEstadoMapaRuta())
    val estadoMapaRuta: StateFlow<TrovareEstadoMapaRuta> = _estadoMapaRuta.asStateFlow()

    fun setOrigenRuta(nuevoValor: LatLng) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                origenRuta = nuevoValor,
            )
        }
    }

    fun setDestinoRuta(nuevoValor: LatLng) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                destinoRuta = nuevoValor,
            )
        }
    }

    fun setPolilineaCodRuta(nuevoValor: String) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                polilineaCodRuta = nuevoValor,
            )
        }
    }

    fun setPolilineaInicializadaRuta(nuevoValor: Boolean) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                polilineaInicializadaRuta = nuevoValor,
            )
        }
    }

    fun setZoomRuta(nuevoValor: Float) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                zoomRuta = nuevoValor,
            )
        }
    }

    fun setMarcadorInicializadoRuta(nuevoValor: Boolean) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                marcadorInicializadoRuta = nuevoValor,
            )
        }
    }

    fun setNombreLugarRuta(nuevoValor: String) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                nombreLugarRuta = nuevoValor,
            )
        }
    }

    fun setIdLugarRuta(nuevoValor: String) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                idLugarRuta = nuevoValor,
            )
        }
    }

    fun setDistanciaEntrePuntos(nuevoValor: Float) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                distanciaEntrePuntos = nuevoValor,
            )
        }
    }

    fun setTiempoDeViaje(nuevoValor: String) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                tiempoDeViaje = nuevoValor,
            )
        }
    }

    fun setTransporteRuta(nuevoValor: String) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                transporteRuta = nuevoValor,
            )
        }
    }

    fun setImgsInicializadas(nuevoValor: Boolean) {
        _estadoMapaRuta.update { estadoActual ->
            estadoActual.copy(
                imgsInicializadas = nuevoValor,
            )
        }
    }

    fun guardarOrigenRuta(indiceActual: Int, origenNuevo: LatLng) {//Guarda el origen de la ruta para este lugar en especifico
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.origen = origenNuevo
    }

    fun guardarRutaLugar(indiceActual: Int, rutaNueva: String) {//Guarda la linea de la ruta codificada como string
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.ruta = rutaNueva
    }

    fun guardarZoomLugar(indiceActual: Int, nuevoZoom: Float) {//Guarda el zoom necesario para mostrar bien el mapa al abrir la pantalla RutasItinerario
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.zoom = nuevoZoom
    }

    fun guardarTransporte(indiceActual: Int, nuevoTransporte: String) {
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.transporte = nuevoTransporte
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------------------------ITINERARIOS--------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    //itinerario Actual
    private val _itinerarioActual = MutableStateFlow(itinerarioPrueba)
    val itinerarioActual = _itinerarioActual.asStateFlow()

    private val _indiceActual = MutableStateFlow(0)
    val indiceActual = _indiceActual.asStateFlow()

    fun setIndiceActual(nuevoIndice: Int) {
        _indiceActual.value = nuevoIndice
    }

    fun setItinerarioActual(nuevoItinerario: Itinerario) {
        _itinerarioActual.value = nuevoItinerario
    }
    fun setNombreItinerario(nuevoNombre: String){
        _itinerarioActual.value.nombre = nuevoNombre
    }

    fun agregarLugarALItinerario(id: String, nombreLugar: String, ubicacion: LatLng) {
        val lugarNuevo = Lugar(
            id = id,
            nombreLugar =  nombreLugar,
            fechaDeVisita = null,
            horaDeVisita = null,
            origen = null,
            ubicacion = ubicacion,
            ruta = null,
            zoom = 15f,
            imagen=_imagen.value
        )
        val itinerarioActualValor = _itinerarioActual.value

        // Verificar si la lista de lugares existe, si no, crearla
        if (itinerarioActualValor.lugares == null) {
            itinerarioActualValor.lugares = mutableListOf()
        }
        // Agregar el nuevo lugar a la lista de lugares del itinerario actual
        itinerarioActualValor.lugares?.add(lugarNuevo)
        // Actualizar el valor del itinerario actual en MutableStateFlow
        _itinerarioActual.value = itinerarioActualValor
    }
    fun setFechaDeVisita(indiceActual: Int, fechaNueva: LocalDate) {
            val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
            lugarActual?.fechaDeVisita = fechaNueva
    }

    fun setHoraDeVisita(indiceActual: Int, horaNueva: LocalTime) {
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.horaDeVisita = horaNueva
        /*val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.horaDeVisita = horaNueva*/
    }


    fun borrarLugarActual(lugar: Lugar) {
        _itinerarioActual.value.lugares?.remove(lugar)
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------------------------API----------------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //Obtener preview de los lugares incluyendo el nombre, direccion e ID es ApiService.kt
    //Funcion para obtener detalles del lugar con base en un ID de lugar----------------------------
    fun obtenerLugar(
        placesClient: PlacesClient,
        placeId: String,
        nombre: (String?) -> Unit,
        direccion: (String?) -> Unit,
        rating: (Double?) -> Unit,
        numeroTelefono: (String?) -> Unit,
        paginaWeb: (String?) -> Unit,
        latLng: (LatLng?) -> Unit,
    ){
        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.RATING,
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
            Place.Field.LAT_LNG,
            Place.Field.PHOTO_METADATAS
        )//campos que se deben obtener de la API de places
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place


                nombre(place.name)
                direccion(place.address)
                rating(place.rating)
                numeroTelefono(place.phoneNumber)
                latLng(place.latLng)

                if(place.websiteUri != null){
                    paginaWeb(place.websiteUri?.toString())
                }

                // Obtener metadatos de la foto-----------------------------------------------------
                val metada = place.photoMetadatas
                if (metada != null) {

                    val photoMetadata = metada.first()

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(500) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                            val image = fetchPhotoResponse.bitmap
                            val imagenBitmap: ImageBitmap = image.asImageBitmap()
                            _imagen.value = imagenBitmap
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }

    //Obtener info para las rutas
    fun obtenerLugarRuta(
        placesClient: PlacesClient,
        placeId: String,
        nombre: (String?) -> Unit,
        direccion: (String?) -> Unit,
        rating: (Double?) -> Unit,
        numeroTelefono: (String?) -> Unit,
        paginaWeb: (String?) -> Unit,
    ){
        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.RATING,
            Place.Field.PHONE_NUMBER,
            Place.Field.WEBSITE_URI,
            Place.Field.PHOTO_METADATAS
        )//campos que se deben obtener de la API de places
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place


                nombre(place.name)
                direccion(place.address)
                rating(place.rating)
                numeroTelefono(place.phoneNumber)

                if(place.websiteUri != null){
                    paginaWeb(place.websiteUri?.toString())
                }

                // Obtener metadatos de la foto-----------------------------------------------------
                val metada = place.photoMetadatas
                if (metada != null) {

                    val photoMetadata = metada.first()

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(500) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                            val image = fetchPhotoResponse.bitmap
                            val imagenBitmap: ImageBitmap = image.asImageBitmap()
                            _imagen.value = imagenBitmap
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }


    //Funci[on para obtener detalles para lugares cercanos------------------------------------------

    fun obtenerFotoLugarCercano(
        placesClient: PlacesClient,
        placesId: List<String>,
    ){
        val placeFields = listOf(
            Place.Field.PHOTO_METADATAS
        )//campos que se deben obtener de la API de places



        placesId.forEachIndexed{ index, placeId ->

            val request = FetchPlaceRequest.newInstance(placeId, placeFields)

            placesClient.fetchPlace(request)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place

                    // Obtener metadatos de la foto-----------------------------------------------------
                    val metada = place.photoMetadatas
                    if (metada != null) {

                        val photoMetadata = metada.first()

                        // Create a FetchPhotoRequest.
                        val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                            .setMaxWidth(300) // Optional.
                            .setMaxHeight(300) // Optional.
                            .build()
                        placesClient.fetchPhoto(photoRequest)
                            .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                                val image = fetchPhotoResponse.bitmap
                                val imagenBitmap: ImageBitmap = image.asImageBitmap()
                                // Asegúrate de que la lista tenga un tamaño suficiente

                                while (_imagenes.value.size <= index) {
                                    _imagenes.value.add(null)
                                }

                                // Agregar la imagen a la lista
                                _imagenes.value[index] = imagenBitmap

                            }.addOnFailureListener { exception: Exception ->
                                if (exception is ApiException) {
                                    val statusCode = exception.statusCode
                                    TODO("Handle error with given status code.")
                                }
                            }
                    }
                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        val statusCode = exception.statusCode
                        TODO("Handle error with given status code")
                    }
                }
        }
    }

    //funci[on para obtener informacion para el mapa------------------------------------------------
    fun obtenerMarcador(
        placesClient: PlacesClient,
        placeId: String,
    ){

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.NAME,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS
        )//campos que se deben obtener de la API de places

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                setDestino(place.latLng?:LatLng(estadoMapa.value.destino.latitude, estadoMapa.value.destino.longitude))
                setNombreLugar(place.name?:"")
                setRatingLugar(place.rating?:-1.0)
                setIdLugar(place.id?:"")
                setUbicacionLugar(place.latLng?:LatLng(estadoMapa.value.destino.latitude, estadoMapa.value.destino.longitude))

                val metada = place.photoMetadatas
                if (metada != null) {

                    val photoMetadata = metada.first()

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(200) // Optional.
                        .setMaxHeight(200) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                            val image = fetchPhotoResponse.bitmap
                            val imagenBitmap: ImageBitmap = image.asImageBitmap()
                            _imagen.value = imagenBitmap
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                Log.e("testLugar", "Place not found: " + exception.message)
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }

                setMarcadorInicializado(true)
                setInformacionInicializada(true)

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }

    //Funcion para obtener el marcador del origen
    fun obtenerMarcadorOrigen(
        placesClient: PlacesClient,
        placeId: String,
    ){
        val placeFields = listOf(
            Place.Field.LAT_LNG,
        )//campos que se deben obtener de la API de places

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                setOrigenRuta(place.latLng?:LatLng(estadoMapa.value.destino.latitude, estadoMapa.value.destino.longitude))
                //setNombreLugar(place.name?:"")
                //setIdLugar(place.id?:"")

                setMarcadorInicializadoRuta(true)

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }

    fun obtenerMarcadorEntreMuchos(
        placesClient: PlacesClient,
        placeId: String,
    ){
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.RATING,
            Place.Field.PHOTO_METADATAS
        )//campos que se deben obtener de la API de places

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                setNombreLugar(place.name?:"")
                setRatingLugar(place.rating?:-1.0)
                setIdLugar(place.id?:"")

                val metada = place.photoMetadatas
                if (metada != null) {

                    val photoMetadata = metada.first()

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(200) // Optional.
                        .setMaxHeight(200) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                            val image = fetchPhotoResponse.bitmap
                            val imagenBitmap: ImageBitmap = image.asImageBitmap()
                            _imagen.value = imagenBitmap
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }

                setInformacionInicializada(true)

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }
    //--------------------------------------------------------------------------------------------//
    //-------------------------------------FIREBASE-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    private val _usuario = MutableStateFlow(usuarioPrueba)
    val usuario = _usuario.asStateFlow()
    fun setUsuario(nuevoUsuario: Usuario) {
        _usuario.value = nuevoUsuario
    }

    private val _listaItinerarios = MutableStateFlow(mutableListOf<Itinerario>())
    val listaItinerario = _listaItinerarios.asStateFlow()

    fun setListaItinerarios(nuevaListaItinerario: MutableList<Itinerario>){
        _listaItinerarios.value = nuevaListaItinerario
    }

    // Funciones auxiliares
    fun obtenerDato() {
        viewModelScope.launch{

            try{

                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()

                //Log.d("TTTT", firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await().getString("nombre").toString())
                //firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().result.id
                val documento =  firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await()
                val usuario = Usuario(
                        nombre = documento.getString("nombre").toString(),
                        foto_perfil = documento.getString("foto_perfil").toString(),
                        fechaDeRegistro = documento.getString("fechaDeRegistro").toString(),
                        descripcion = documento.getString("descripcion").toString(),
                        lugarDeOrigen = documento.getString("lugarDeOrigen").toString(),
                        comentarios = null,
                        itinerarios = mutableListOf()
                )

                setUsuario(usuario)

            }catch(e: Exception){
                setUsuario(usuarioPrueba)
                //Lista de itinerarioa
            }
        }
    }
}

