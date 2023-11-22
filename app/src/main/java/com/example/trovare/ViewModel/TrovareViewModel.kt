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
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.R
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
    ) {
        try {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        // Aquí tienes la latitud y longitud.
                        // Puedes usar estas variables en tu lógica o pasárselas al ViewModel según sea necesario.
                        setUbicacion(LatLng(latitude, longitude))

                    }
                }
                .addOnFailureListener { exception ->
                    //TODO
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    //ubicaci[on del usuario
    private val _ubicacion = MutableStateFlow(LatLng(19.504507, -99.147314))
    val ubicacion = _ubicacion.asStateFlow()
    // Función para actualizar el valor de la ubicación
    fun setUbicacion(nuevaUbicacion: LatLng) {
        _ubicacion.value = nuevaUbicacion
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

    //mostrar la polilinea de la ruta
    private val _polilineaInicializada = MutableStateFlow(false)
    val polilineaInicializada: StateFlow<Boolean> = _polilineaInicializada.asStateFlow()
    fun setPolilineaInicializada(newValue: Boolean) {
        _polilineaInicializada.value = newValue
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

                setUbicacion(place.latLng?:LatLng(ubicacion.value.latitude, ubicacion.value.longitude))
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
                        foto_perfil = R.drawable.perfil,
                        fechaDeRegistro = documento.getString("fechaDeRegistro").toString(),
                        descripcion = documento.getString("descripcion").toString(),
                        lugarDeOrigen = documento.getString("lugarDeOrigen").toString(),
                        comentarios = null
                )

                setUsuario(usuario)

            }catch(e: Exception){

                setUsuario(usuarioPrueba)

            }
        }
    }
}
