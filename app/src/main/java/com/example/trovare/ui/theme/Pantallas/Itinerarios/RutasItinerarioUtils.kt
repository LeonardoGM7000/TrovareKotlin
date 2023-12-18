package com.example.trovare.ui.theme.Pantallas.Itinerarios

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trovare.ViewModel.TrovareViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient

fun obtenerMarcadorOrigen(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel
){
    val placeFields = listOf(
        Place.Field.LAT_LNG,
    )//campos que se deben obtener de la API de places

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place

            viewModel.setOrigenRuta(place.latLng?: LatLng(viewModel.estadoMapa.value.destino.latitude, viewModel.estadoMapa.value.destino.longitude))
            viewModel.setMarcadorInicializadoRuta(true)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("testLugar", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}

//Obtener info para las rutas
fun obtenerLugarRuta(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel
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

            viewModel.setNombreRuta(place.name?:"")
            viewModel.setDireccionRuta(place.address)
            viewModel.setRatingRuta(place.rating)
            viewModel.setTelefonoRuta(place.phoneNumber)

            if(place.websiteUri != null){
                viewModel.setPaginaWebRuta(place.websiteUri.toString())
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
                        viewModel.setImagenRuta(imagenBitmap)
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