package com.example.trovare.ViewModel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [TrovareViewModel] guarda información de la aplicación dentro del ciclo de vida.
 */

class TrovareViewModel : ViewModel() {
    //Manejo de imagenes----------------------------------------------------------------------------
    private val _imagen = mutableStateOf<ImageBitmap?>(null)
    val imagen: State<ImageBitmap?> = _imagen

    private val _imagenes = mutableStateOf(mutableListOf<ImageBitmap?>())
    val imagenes: State<MutableList<ImageBitmap?>?> = _imagenes

    fun reiniciarImagen() {
        _imagen.value = null
    } // Asegura que la corrutina se cancele al salir del composable

    //guardar variables de estado de la UI----------------------------------------------------------

    private val _estadoUi = MutableStateFlow(TrovareEstadoUi())
    val uiState: StateFlow<TrovareEstadoUi> = _estadoUi.asStateFlow()

    //Pantalla-configuración------------------------------------------------------------------------
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

    //Pantalla-soporte------------------------------------------------------------------------------

    fun setResultoUtil(nuevaSeleccion: String){
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                resultoUtil = nuevaSeleccion
            )
        }
    }

    //Ubicacion-------------------------------------------------------------------------------------


    private val _ubicacion = MutableStateFlow(LatLng(19.504507, -99.147314))
    val ubicacion = _ubicacion.asStateFlow()
    // Función para actualizar el valor de la ubicación
    fun setUbicacion(nuevaUbicacion: LatLng) {
        _ubicacion.value = nuevaUbicacion
        Log.i("pruebaqas","${ubicacion}")
    }
    //Controlar la navegaci[on del mapa-------------------------------------------------------------
    private val _visible = MutableStateFlow(true)
    val visible: StateFlow<Boolean> = _visible.asStateFlow()

    fun setVisible(newValue: Boolean) {
        _visible.value = newValue
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
        //imagen: (ImageBitmap) -> Unit
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
                    paginaWeb(place.websiteUri.toString())
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
                                Log.e("testLugar", "Place not found: " + exception.message)
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
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

        Log.e("testLugar", "entrada")


        placesId.forEachIndexed{ index, placeId ->
            Log.e("testLugar", "entrada2")

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
                                Log.e("testLugar", "entrada3")
                                val image = fetchPhotoResponse.bitmap
                                val imagenBitmap: ImageBitmap = image.asImageBitmap()
                                Log.e("testLugar", "entrada4")
                                // Asegúrate de que la lista tenga un tamaño suficiente

                                while (_imagenes.value.size <= index) {
                                    _imagenes.value.add(null)
                                }
                                Log.e("testLugar", "entrada5")

                                // Agregar la imagen a la lista
                                _imagenes.value[index] = imagenBitmap
                                Log.e("testLugar", "entrada6")




                                Log.e("testLugar", "exito cargada imagen ${index}")
                                Log.e("testLugar", "imagen imagen ${imagenes.value?.get(index)}")

                            }.addOnFailureListener { exception: Exception ->
                                if (exception is ApiException) {
                                    Log.e("testLugar", "Place not found: " + exception.message)
                                    val statusCode = exception.statusCode
                                    TODO("Handle error with given status code.")
                                }
                            }
                    }

                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {
                        Log.e("testLugar", "Place not found: ${exception.message}")
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
            Place.Field.LAT_LNG,
        )//campos que se deben obtener de la API de places

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                setUbicacion(place.latLng)
                setVisible(true)

            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }
}