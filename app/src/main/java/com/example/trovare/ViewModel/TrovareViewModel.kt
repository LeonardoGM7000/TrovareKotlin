package com.example.trovare.ViewModel

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trovare.Data.Hora
import com.example.trovare.Data.Itinerario
import com.example.trovare.Data.Lugar
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.itinerarioPrueba
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
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

/**
 * [TrovareViewModel] guarda información de la aplicación dentro del ciclo de vida.
 */
//F por Uzias

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

    private val _estadoUi = MutableStateFlow(TrovareEstadoUi())
    val uiState: StateFlow<TrovareEstadoUi> = _estadoUi.asStateFlow()

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
                        setOrigen(LatLng(latitude, longitude))
                        setOrigenRuta(LatLng(latitude, longitude))

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
    //-------------------------------------MAPA---------------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //MAPA PRINCIPAL y MAPA AGREGAR LUGAR A ITINERARIO----------------------------------------------


    //Origen y destino para mapa principal y seleccion de lugar
    //Ubicacion del usuario
    private val _origen = MutableStateFlow(LatLng(19.504507, -99.147314))
    val origen = _origen.asStateFlow()

    fun setOrigen(nuevaUbicacion: LatLng) {
        _origen.value = nuevaUbicacion
    }

    //ubicacion del destino
    private val _destino = MutableStateFlow(LatLng(19.504507, -99.147314))
    val destino = _destino.asStateFlow()
    // Función para actualizar el valor de la ubicación

    fun setDestino(nuevaUbicacion: LatLng) {
        _destino.value = nuevaUbicacion
    }


    //mostrar la polilinea de la ruta
    //Guardar la polilinea codificada
    private val _polilineaCod = MutableStateFlow("")

    val polilineaCod = _polilineaCod.asStateFlow()

    fun setPolilineaCod(newValue: String) {
        _polilineaCod.value = newValue
    }

    //mostrar la polilinea de la ruta
    private val _polilineaInicializada = MutableStateFlow(false)
    val polilineaInicializada: StateFlow<Boolean> = _polilineaInicializada.asStateFlow()
    fun setPolilineaInicializada(newValue: Boolean) {
        _polilineaInicializada.value = newValue
    }

    //zoom del mapa
    private val _zoom = MutableStateFlow(15f)
    val zoom = _zoom.asStateFlow()
    fun setZoom(nuevoZoom: Float) {
        _zoom.value = nuevoZoom
    }

    //para mostrar el marcador de un solo lugar
    private val _marcadorInicializado = MutableStateFlow(false)
    val marcadorInicializado: StateFlow<Boolean> = _marcadorInicializado.asStateFlow()
    fun setMarcadorInicializado(newValue: Boolean) {
        _marcadorInicializado.value = newValue
    }
    //mostrar los marcadores de varios lugares
    private val _marcadoresInicializado = MutableStateFlow(false)
    val marcadoresInicializado: StateFlow<Boolean> = _marcadoresInicializado.asStateFlow()
    fun setMarcadoresInicializado(newValue: Boolean) {
        _marcadoresInicializado.value = newValue
    }

    //mostrar la tarjeta de inofmracion del lugar
    private val _informacionInicializada = MutableStateFlow(false)
    val informacionInicializada: StateFlow<Boolean> = _informacionInicializada.asStateFlow()
    fun setInformacionInicializada(newValue: Boolean) {
        _informacionInicializada.value = newValue
    }

    //nombre del lugar seleccionando
    private val _nombreLugar = MutableStateFlow("")
    val nombreLugar = _nombreLugar.asStateFlow()

    fun setNombreLugar(nuevoNombre: String) {
        _nombreLugar.value = nuevoNombre
    }
    //rating del lugar seleccionado
    private val _ratingLugar = MutableStateFlow(-1.0)
    val ratingLugar = _ratingLugar.asStateFlow()

    fun setRatingLugar(nuevoRating: Double) {
        _ratingLugar.value = nuevoRating
    }
    //id del lugar seleccionado
    private val _idLugar = MutableStateFlow("")
    val idLugar = _idLugar.asStateFlow()

    fun setIdLugar(nuevoId: String) {
        _idLugar.value = nuevoId
    }

    private val _ubicacionLugar = MutableStateFlow(LatLng(0.0,0.0))
    val ubicacionLugar = _ubicacionLugar.asStateFlow()

    fun setUbicacionLugar(nuevaUbicacion: LatLng) {
        _ubicacionLugar.value = nuevaUbicacion
    }

    //MAPA SELECCION DE RUTAS EN ITINERARIO---------------------------------------------------------

    //origen y destino para seleccion de ruta
    //Ubicacion del usuario
    private val _origenRuta = MutableStateFlow(LatLng(19.504507, -99.147314))
    val origenRuta = _origenRuta.asStateFlow()

    fun setOrigenRuta(nuevaUbicacion: LatLng) {
        _origenRuta.value = nuevaUbicacion
    }

    //ubicaci[on del usuario
    private val _destinoRuta = MutableStateFlow(LatLng(19.504507, -99.147314))
    val destinoRuta = _destinoRuta.asStateFlow()
    // Función para actualizar el valor de la ubicación
    fun setDestinoRuta(nuevaUbicacion: LatLng) {
        _destinoRuta.value = nuevaUbicacion
    }


    private val _polilineaCodRuta = MutableStateFlow("")
    val polilineaCodRuta = _polilineaCodRuta.asStateFlow()
    fun setPolilineaCodRuta(newValue: String) {
        _polilineaCodRuta.value = newValue
    }

    private val _polilineaInicializadaRuta = MutableStateFlow(false)
    val polilineaInicializadaRuta: StateFlow<Boolean> = _polilineaInicializadaRuta.asStateFlow()
    fun setPolilineaInicializadaRuta(newValue: Boolean) {
        _polilineaInicializadaRuta.value = newValue
    }

    //zoom del mapa
    private val _zoomRuta = MutableStateFlow(15f)
    val zoomRuta = _zoomRuta.asStateFlow()
    fun setZoomRuta(nuevoZoom: Float) {
        _zoomRuta.value = nuevoZoom
    }

    //para mostrar el marcador de un solo lugar
    private val _marcadorInicializadoRuta = MutableStateFlow(false)
    val marcadorInicializadoRuta: StateFlow<Boolean> = _marcadorInicializadoRuta.asStateFlow()
    fun setMarcadorInicializadoRuta(newValue: Boolean) {
        _marcadorInicializadoRuta.value = newValue
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

    //nombre del lugar seleccionando
    private val _nombreLugarRuta = MutableStateFlow("")
    val nombreLugarRuta = _nombreLugarRuta.asStateFlow()

    fun setNombreLugarRuta(nuevoNombre: String) {
        _nombreLugarRuta.value = nuevoNombre
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

    fun setHoraDeVisita(indiceActual: Int, horaNueva: Hora) {
        val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.horaDeVisita = horaNueva
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

                setDestino(place.latLng?:LatLng(destino.value.latitude, destino.value.longitude))
                setNombreLugar(place.name?:"")
                setRatingLugar(place.rating?:-1.0)
                setIdLugar(place.id?:"")
                setUbicacionLugar(place.latLng?:LatLng(destino.value.latitude, destino.value.longitude))

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

                setOrigenRuta(place.latLng?:LatLng(destino.value.latitude, destino.value.longitude))
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

