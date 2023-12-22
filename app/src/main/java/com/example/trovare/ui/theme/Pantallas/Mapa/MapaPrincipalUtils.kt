package com.example.trovare.ui.theme.Pantallas.Mapa

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trovare.Api.APIServiceSolicitaPolyline
import com.example.trovare.Data.NearbyLocationsClass
import com.example.trovare.Data.Routes
import com.example.trovare.ViewModel.TrovareViewModel
import com.google.android.gms.common.api.ApiException
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
import kotlinx.coroutines.launch
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
import kotlin.math.pow

fun calcularZoom(
    punto1: LatLng,
    punto2: LatLng,
    viewModel: TrovareViewModel
) {
    val latDiff = punto2.latitude - punto1.latitude
    val lonDiff = punto2.longitude - punto1.longitude

    val distancia = Math.sqrt(latDiff.pow(2) + lonDiff.pow(2))
    Log.d("distancia" ,"${distancia}")

    when {
        distancia > 10 -> {
            viewModel.setZoom(5f)
        }
        distancia > 6 -> {
            viewModel.setZoom(6f)
        }
        distancia > 2 -> {
            viewModel.setZoom(7f)
        }
        distancia > 1 -> {
            viewModel.setZoom(8f)
        }
        distancia > 0.5 -> {
            viewModel.setZoom(9f)
        }
        distancia > 0.3 -> {
            viewModel.setZoom(10f)
        }
        distancia > 0.15 -> {
            viewModel.setZoom(11f)
        }
        distancia > 0.085 -> {
            viewModel.setZoom(12f)
        }
        distancia > 0.04 -> {
            viewModel.setZoom(13f)
        }
        distancia > 0.0 -> {
            viewModel.setZoom(14f)
        }
    }
}

//API-----------------------------------------------------------------------------------------------

//obtener la polilinea de la ruta para el mapa principal
fun apiRutasMapaPrincipal(
    destino: LatLng,
    origen: LatLng,
    viewModel: TrovareViewModel,
    travel_mode: String = "DRIVE"
    //recuperarResultados: MutableList<RutaInfo>
) {

    // Crear Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://routes.googleapis.com")
        .build()

    // Crear Servicio
    val service = retrofit.create(APIServiceSolicitaPolyline::class.java)

    // Crear JSON usando JSONObject
    val jsonObject = JSONObject()

    val origin = JSONObject()
    val locationo = JSONObject()
    val latLngo = JSONObject()

    latLngo.put("latitude", destino.latitude)
    latLngo.put("longitude", destino.longitude)

    locationo.put("latLng", latLngo)

    origin.put("location", locationo)
    jsonObject.put("origin", origin)

    val destination = JSONObject()
    val locationd = JSONObject()
    val latLngd = JSONObject()

    latLngd.put("latitude", origen.latitude)
    latLngd.put("longitude", origen.longitude)

    locationd.put("latLng", latLngd)

    destination.put("location", locationd)
    jsonObject.put("destination", destination)

    jsonObject.put("travelMode", "${travel_mode}")
    jsonObject.put("computeAlternativeRoutes", false)
    jsonObject.put("units", "IMPERIAL")

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()

    Log.d("MandarJSONruta",jsonObjectString)

    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        // Hacer el request POST y obtener respuesta
        val response = service.getRuta(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                // Convertir raw JSON a pretty JSON usando la libreria GSON
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // : https://github.com/square/retrofit/issues/3255
                    )
                )

                Log.d("Pretty Printed JSON rutasss:", prettyJson)

                val gson1 = Gson()
                val mUser = gson1.fromJson(prettyJson, Routes::class.java)
                val rutaInfo = RutaInfo(
                    distancia = mUser.routes.first().distance,
                    duracion = mUser.routes.first().duration,
                    polilinea = mUser.routes.first().polyline.encodPolyline
                )

                viewModel.setPolilineaCod(rutaInfo.polilinea)
                viewModel.setTiempoDeViaje(rutaInfo.duracion)
                viewModel.setDistanciaEntrePuntos(rutaInfo.distancia)
                viewModel.setPolilineaInicializada(true)


            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
    }
}


fun obtenerMarcador(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel
){

    val placeFields = listOf(
        Place.Field.ID,
        Place.Field.LAT_LNG,
        Place.Field.NAME,
        Place.Field.RATING,
        Place.Field.PHOTO_METADATAS,
    )//campos que se deben obtener de la API de places

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place

            viewModel.setDestino(place.latLng?: LatLng(viewModel.estadoMapa.value.destino.latitude, viewModel.estadoMapa.value.destino.longitude))
            viewModel.setNombreLugar(place.name?:"")
            viewModel.setRatingLugar(place.rating)
            viewModel.setIdLugar(place.id?:"")
            viewModel.setUbicacionLugar(place.latLng?: LatLng(viewModel.estadoMapa.value.destino.latitude, viewModel.estadoMapa.value.destino.longitude))

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
                        viewModel.setImagenLugar(imagenBitmap)
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("testLugar", "Place not found: " + exception.message)
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }
            }

            viewModel.setMarcadorInicializado(true)
            viewModel.setInformacionInicializada(true)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("testLugar", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}


//Recuperar LatLng para lugares cercanos------------------------------------------------------------
interface APIServiceBuscarUbicacionesCercanas {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc",
        "X-Goog-FieldMask: places.location,places.id",
    )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearbyLocation(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}


fun rawJSONUbicacionesCercanas(
    filtro: String,
    viewModel: TrovareViewModel,
    ubicacion: LatLng
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
    val retrofit = Retrofit.Builder()
        .baseUrl("https://places.googleapis.com")
        .build()

    // Crear Servicio
    val service = retrofit.create(APIServiceBuscarUbicacionesCercanas::class.java)

    // Crear JSON usando JSONObject

    val jsonObject = JSONObject()
    jsonObject.put("includedTypes", JSONArray().put(traducirOpcion(filtro)))
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("languageCode", "es")

    val locationRestriction = JSONObject()
    val circle = JSONObject()
    val center = JSONObject()

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

    //recuperarResultados.clear()

    CoroutineScope(Dispatchers.IO).launch {

        //Hacer el request POST y obtener respuesta
        val response = service.createPlaceNearbyLocation(requestBody)
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                // Convertir raw JSON a pretty JSON usando la libreria GSON
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // : https://github.com/square/retrofit/issues/3255
                    )
                )

                Log.d("Pretty Printed JSON :", prettyJson)

                val gson1 = Gson()
                var mUser = gson1.fromJson(prettyJson, NearbyLocationsClass::class.java)

                viewModel.estadoMapa.value.marcadores.clear()

                mUser.nearbyLocations.forEach { lugar ->
                    if (lugar != null) {
                        viewModel.estadoMapa.value.marcadores.add(Marcador(ubicacion = LatLng(lugar.location.latitude, lugar.location.longitude), id = lugar.id))
                    } else {
                        //TODO No se encontraorn resultados
                    }
                }
                viewModel.setMarcadoresInicializado(true)
            } else {
                //TODO Error retrofit
            }
        }
    }
}

fun obtenerMarcadorEntreMuchos(
    placesClient: PlacesClient,
    placeId: String,
    viewModel: TrovareViewModel
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

            viewModel.setNombreLugar(place.name?:"")
            viewModel.setRatingLugar(place.rating?:-1.0)
            viewModel.setIdLugar(place.id?:"")

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

                        val imagenBitmap: ImageBitmap = fetchPhotoResponse.bitmap.asImageBitmap()

                        viewModel.setImagenLugar(imagenBitmap)
                        viewModel.setInformacionInicializada(true)

                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
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