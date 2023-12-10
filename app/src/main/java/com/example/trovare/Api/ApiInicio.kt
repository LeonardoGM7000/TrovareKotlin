package com.example.trovare.Api

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.NearbyPlacesClass
import com.example.trovare.ViewModel.TrovareViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


//API BUSCAR POR CATEGORIAS-------------------------------------------------------------------------
private interface ApiBuscarPorCategorias {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ",
        "X-Goog-FieldMask: places.id,places.displayName,places.rating,places.primaryType",
    )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearby(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}



//Buscar por la categoria del lugar-----------------------------------------------------------------
fun apiBuscarPorCategorias(
    filtro: String,
    ubicacion: LatLng,
    viewModel: TrovareViewModel,
    placesClient: PlacesClient
) {

    fun traducirOpcion(opcion: String): String {
        return when (opcion) {
            "Restaurantes" -> "restaurant"
            "Museos" -> "museum"
            "Parques" -> "park"
            "Hoteles" -> "hotel"
            else -> "tourist_attraction"
        }
    }

    // Crear Retrofit
    val retrofit = Retrofit.Builder().baseUrl("https://places.googleapis.com").build()
    // Crear Servicio
    val service = retrofit.create(ApiBuscarPorCategorias::class.java)
    // Crear JSON usando JSONObject y agregar objetos al json
    val jsonObject = JSONObject()
    val locationRestriction = JSONObject()
    val circle = JSONObject()
    val center = JSONObject()
    jsonObject.put("includedTypes", JSONArray().put(traducirOpcion(filtro)))
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("languageCode", "es")
    center.put("latitude", ubicacion.latitude)
    center.put("longitude", ubicacion.longitude)
    circle.put("center", center)
    circle.put("radius", 5000.0)
    locationRestriction.put("circle", circle)
    jsonObject.put("locationRestriction", locationRestriction)

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()
    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        //Hacer el request POST y obtener respuesta

        //TODO AGREGAR VERIFICAR LA CONEXION CON INTERNET

        val response = service.createPlaceNearby(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                val listaLugaresCercanos: MutableList<NearbyPlaces?> = mutableListOf()

                // Convertir raw JSON a pretty JSON usando la libreria GSON
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // : https://github.com/square/retrofit/issues/3255
                    )
                )

                //Log.d("Pretty Printed JSON :", prettyJson)

                val gson1 = Gson()
                val mUser = gson1.fromJson(prettyJson, NearbyPlacesClass::class.java)
                mUser.placesNearby.forEach { lugar ->

                    coroutineScope {
                        Log.d("testImagenes","primero")
                        obtenerImagenCategoria(placeId = lugar!!.id, placesClient = placesClient, viewModel = viewModel)
                        Log.d("testImagenes","ultimo")
                        val imagen = viewModel.estadoInicial.value.imagenTemporalCategoria
                        listaLugaresCercanos.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType, imagen = imagen))
                    }
                }
                viewModel.setLugaresCercanos(listaLugaresCercanos)//agregar a la lista de lugares cercanos
                viewModel.setLugaresCercanosInicializado(true)
            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                //TODO MANEJAR ERROR DE RETROFIT
            }
        }
    }
}

//API Buscar por mas popoular-----------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
fun apiBuscarPorPopularidad(
    ubicacion: LatLng,
    viewModel: TrovareViewModel,
    placesClient: PlacesClient
) {

    // Crear Retrofit
    val retrofit = Retrofit.Builder().baseUrl("https://places.googleapis.com").build()
    // Crear Servicio
    val service = retrofit.create(ApiBuscarPorCategorias::class.java)
    // Crear JSON usando JSONObject y agregar objetos al json
    val jsonObject = JSONObject()
    val locationRestriction = JSONObject()
    val circle = JSONObject()
    val center = JSONObject()
    jsonObject.put("includedTypes", JSONArray().put("tourist_attraction"))
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("languageCode", "es")
    center.put("latitude", ubicacion.latitude)
    center.put("longitude", ubicacion.longitude)
    circle.put("center", center)
    circle.put("radius", 5000.0)
    locationRestriction.put("circle", circle)
    jsonObject.put("locationRestriction", locationRestriction)

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()
    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(IO).launch {
        //Hacer el request POST y obtener respuesta

        //TODO AGREGAR VERIFICAR LA CONEXION CON INTERNET

        val response = service.createPlaceNearby(requestBody)
        withContext(Main) {
            if (response.isSuccessful) {

                val listaLugaresPopulares: MutableList<NearbyPlaces?> = mutableListOf()

                // Convertir raw JSON a pretty JSON usando la libreria GSON
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() //
                    )
                )

                //Log.d("Pretty Printed JSON :", prettyJson)

                val gson1 = Gson()
                val mUser = gson1.fromJson(prettyJson, NearbyPlacesClass::class.java)
                mUser.placesNearby.forEach { lugar ->

                    coroutineScope {
                        Log.d("testImagenes","primero")
                        obtenerImagenPopulares(placeId = lugar!!.id, placesClient = placesClient, viewModel = viewModel)
                        Log.d("testImagenes","ultimo")
                        val imagen = viewModel.estadoInicial.value.imagenTemporalPopulares
                        listaLugaresPopulares.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType, imagen = imagen))
                        viewModel.setImagenTemporalPopulares(null)
                    }
                }
                viewModel.setLugaresPopulares(listaLugaresPopulares)//agregar a la lista de lugares cercanos
                viewModel.setLugaresPopularesInicializado(true)
            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                //TODO MANEJAR ERROR DE RETROFIT
            }
        }
    }
}

//API Buscar Puntos de interés----------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------
fun apiBuscarPuntosDeInteres(
    ubicacion: LatLng,
    viewModel: TrovareViewModel,
    placesClient: PlacesClient
) {

    // Crear Retrofit
    val retrofit = Retrofit.Builder().baseUrl("https://places.googleapis.com").build()
    // Crear Servicio
    val service = retrofit.create(ApiBuscarPorCategorias::class.java)
    // Crear JSON usando JSONObject y agregar objetos al json
    val jsonObject = JSONObject()
    val locationRestriction = JSONObject()
    val circle = JSONObject()
    val center = JSONObject()
    jsonObject.put("includedTypes", JSONArray().put("tourist_attraction"))
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("rankPreference", "DISTANCE")
    jsonObject.put("languageCode", "es")
    center.put("latitude", ubicacion.latitude)
    center.put("longitude", ubicacion.longitude)
    circle.put("center", center)
    circle.put("radius", 5000.0)
    locationRestriction.put("circle", circle)
    jsonObject.put("locationRestriction", locationRestriction)

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()
    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        //Hacer el request POST y obtener respuesta

        //TODO AGREGAR VERIFICAR LA CONEXION CON INTERNET

        val response = service.createPlaceNearby(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                val listaPuntosDeInteres: MutableList<NearbyPlaces?> = mutableListOf()

                // Convertir raw JSON a pretty JSON usando la libreria GSON
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() //
                    )
                )

                //Log.d("Pretty Printed JSON :", prettyJson)

                val gson1 = Gson()
                val mUser = gson1.fromJson(prettyJson, NearbyPlacesClass::class.java)
                mUser.placesNearby.forEach { lugar ->

                    coroutineScope {
                        Log.d("testImagenes","primero")
                        obtenerImagenPuntosDeInteres(placeId = lugar!!.id, placesClient = placesClient, viewModel = viewModel)
                        Log.d("testImagenes","ultimo")
                        val imagen = viewModel.estadoInicial.value.imagenTemporalPuntosDeInteres
                        listaPuntosDeInteres.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType, imagen = imagen))
                        viewModel.setImagenTemporalPopulares(null)
                    }
                }
                viewModel.setLugaresPuntosDeInteres(listaPuntosDeInteres)//agregar a la lista de lugares cercanos
                viewModel.setLugaresPuntosDeInteresInicializado(true)
            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())
                //TODO MANEJAR ERROR DE RETROFIT
            }
        }
    }
}


suspend fun obtenerImagenCategoria(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel,
) {
    val placeFields = listOf(Place.Field.PHOTO_METADATAS)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    try {
        val response = withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<FetchPlaceResponse> { continuation ->
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        continuation.resume(response)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
        val place = response.place
        val metada = place.photoMetadatas
        if (metada != null) {
            val photoMetadata = metada.first()

            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .build()

            val fetchPhotoResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<FetchPhotoResponse> { continuation ->
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { response ->
                            continuation.resume(response)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
            }
            val image = fetchPhotoResponse.bitmap
            val imagenBitmap: ImageBitmap = image.asImageBitmap()
            viewModel.setImagenTemporalCategoria(imagenBitmap)
        }

    } catch (exception: Exception) {
        // Manejar cualquier excepción aquí
    }
}

suspend fun obtenerImagenPopulares(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel,
) {
    val placeFields = listOf(Place.Field.PHOTO_METADATAS)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    try {
        val response = withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<FetchPlaceResponse> { continuation ->
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        continuation.resume(response)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }
        val place = response.place
        val metada = place.photoMetadatas
        if (metada != null) {
            val photoMetadata = metada.first()

            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .build()

            val fetchPhotoResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<FetchPhotoResponse> { continuation ->
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { response ->
                            continuation.resume(response)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
            }

            val image = fetchPhotoResponse.bitmap
            val imagenBitmap: ImageBitmap = image.asImageBitmap()

            viewModel.setImagenTemporalPopulares(imagenBitmap)
        }
    } catch (exception: Exception) {
        // Manejar cualquier excepción aquí
    }
}


suspend fun obtenerImagenPuntosDeInteres(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel,
) {
    val placeFields = listOf(Place.Field.PHOTO_METADATAS)
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    try {
        val response = withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<FetchPlaceResponse> { continuation ->
                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        continuation.resume(response)
                    }
                    .addOnFailureListener { exception ->
                        continuation.resumeWithException(exception)
                    }
            }
        }

        val place = response.place

        val metada = place.photoMetadatas
        if (metada != null) {
            val photoMetadata = metada.first()

            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .build()

            val fetchPhotoResponse = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine<FetchPhotoResponse> { continuation ->
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { response ->
                            continuation.resume(response)
                        }
                        .addOnFailureListener { exception ->
                            continuation.resumeWithException(exception)
                        }
                }
            }

            val image = fetchPhotoResponse.bitmap
            val imagenBitmap: ImageBitmap = image.asImageBitmap()

            viewModel.setImagenTemporalPuntosDeInteres(imagenBitmap)
        }
    } catch (exception: Exception) {
        // Manejar cualquier excepción aquí
    }
}