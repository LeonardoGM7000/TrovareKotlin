package com.example.trovare.Api

import android.util.Log
import androidx.compose.runtime.MutableFloatState
import com.example.trovare.Data.NearbyLocationsClass
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.NearbyPlacesClass
import com.example.trovare.Data.Places
import com.example.trovare.Data.PlacesClass
import com.example.trovare.Data.Routes
import com.example.trovare.Data.SnappedPointsClass
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador
import com.example.trovare.ui.theme.Pantallas.Mapa.RutaInfo
import com.google.android.gms.maps.model.LatLng
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
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface APIServiceBuscarTexto {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ",
        "X-Goog-FieldMask: places.displayName,places.formattedAddress,places.id"
    )
    @POST("/v1/places:searchText")
    suspend fun createPlace(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun rawJSON(
    query: String,
    recuperarResultados: MutableList<Places>
) {

    // Crear Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://places.googleapis.com")
        .build()

    // Crear Servicio
    val service = retrofit.create(APIServiceBuscarTexto::class.java)

    // Crear JSON usando JSONObject
    val jsonObject = JSONObject()
    jsonObject.put("textQuery", query)
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("languageCode", "es")

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()

    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    recuperarResultados.clear()

    CoroutineScope(Dispatchers.IO).launch {
        // Hacer el request POST y obtener respuesta
        val response = service.createPlace(requestBody)

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
                val mUser = gson1.fromJson(prettyJson, PlacesClass::class.java)
                mUser.places.forEach { lugar ->
                    if (lugar != null) {
                        recuperarResultados.add(Places(id = lugar.id, formattedAddress = lugar.formattedAddress, displayName = lugar.displayName))
                    } else {
                        //manejar error no se eonctraron resultados
                    }
                }


            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
    }
}
//Buscar lugares cercanos por ubicacion-------------------------------------------------------------

//Radio de busqueda-----------------------------
interface APIServiceBuscarPorUbicacion {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ",
        "X-Goog-FieldMask: places.shortFormattedAddress,places.displayName,places.id",

    )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearby(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun rawJSONLugarCercano(
    filtro: String,
    recuperarResultados: MutableList<NearbyPlaces>,
    recuperarId: MutableList<String>,
    ubicacion: LatLng
) {

    fun traducirOpcion(opcion: String): String {
        return when (opcion) {
            "Restaurantes" -> "restaurant"
            "Atracciones" -> "tourist_attraction"
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
    val service = retrofit.create(APIServiceBuscarPorUbicacion::class.java)

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
        val response = service.createPlaceNearby(requestBody)
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
                val mUser = gson1.fromJson(prettyJson, NearbyPlacesClass::class.java)
                mUser.placesNearby.forEach { lugar ->
                    if (lugar != null) {
                        recuperarResultados.add(NearbyPlaces(id = lugar.id, displayName = lugar.displayName, shortFormattedAddress = lugar.shortFormattedAddress))
                        recuperarId.add(lugar.id)
                    } else {
                        //manejar error no se eonctraron resultados
                    }
                }
            } else {
                Log.e("RETROFIT_ERROR", response.code().toString())
            }
        }
    }
}
//Recuperar LatLng para lugares cercanos------------------------------------------------------------
interface APIServiceBuscarUbicacionesCercanas {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ",
        "X-Goog-FieldMask: places.location,places.id",
        )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearbyLocation(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun rawJSONUbicacionesCercanas(
    filtro: String,
    recuperarResultados: MutableList<Marcador>,
    viewModel: TrovareViewModel,
    ubicacion: LatLng
) {



    fun traducirOpcion(opcion: String): String {
        return when (opcion) {
            "Restaurantes" -> "restaurant"
            "Atracciones" -> "tourist_attraction"
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
                val mUser = gson1.fromJson(prettyJson, NearbyLocationsClass::class.java)

                recuperarResultados.clear()

                mUser.nearbyLocations.forEach { lugar ->
                    if (lugar != null) {
                        recuperarResultados.add(Marcador(ubicacion = LatLng(lugar.location.latitude, lugar.location.longitude), id = lugar.id))
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

interface ApiServiceCrearRuta{
    @GET("/v1/snapToRoads")
    suspend fun getPuntosParaRuta(@Query("interpolate") interpolate: Boolean?, @Query(value="path", encoded = true) path: String, @Query("key") key: String): Response<ResponseBody>
}

fun rawJSONCrearRuta(
    path: String,
    interpolate: Boolean?,
    recuperarResultados: MutableList<LatLng>
){
    //Create Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://roads.googleapis.com")
        .build()

   // Create Sevice
    val service = retrofit.create(ApiServiceCrearRuta::class.java)
    val key = "AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc"

    CoroutineScope(Dispatchers.IO).launch {
        // Do the GET request and get response
        val response = service.getPuntosParaRuta(interpolate, path, key)

        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {

                // Convert raw JSON to pretty JSON using GSON library
                val gson = GsonBuilder().setPrettyPrinting().create()
                val prettyJson = gson.toJson(
                    JsonParser.parseString(
                        response.body()
                            ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                )

                Log.d("Pretty Printed JSON Rutas:", prettyJson)

                val gson1 = Gson()
                //val mUser = gson1.fromJson(prettyJson, SnappedPointsClass::class.java)

                try {
                    val mUser = gson1.fromJson(prettyJson, SnappedPointsClass::class.java)
                    //Log.d("Deserialization Success", mUser.toString())
                    mUser.snappedPoints.forEach { punto ->
                        if (punto != null) {
                            recuperarResultados.add(LatLng(punto.location.latitude, punto.location.longitude))
                        } else {
                            //manejar error no se eonctraron resultados
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Deserialization Error", e.message ?: "Unknown error")
                }


            } else {
                Log.e("RETROFIT_ERROR_RUTAS", response.code().toString())

            }
        }
    }
}

interface APIServiceSolicitaPolyline {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc",
        "X-Goog-FieldMask: routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline"
    )
    @POST("/directions/v2:computeRoutes")
    suspend fun getRuta(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun rawJSONRutas(
    origen: LatLng,
    destino: LatLng,
    recuperarResultados: MutableList<RutaInfo>
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

    latLngo.put("latitude", origen.latitude)
    latLngo.put("longitude", origen.longitude)

    locationo.put("latLng", latLngo)

    origin.put("location", locationo)
    jsonObject.put("origin", origin)

    val destination = JSONObject()
    val locationd = JSONObject()
    val latLngd = JSONObject()

    latLngd.put("latitude", destino.latitude)
    latLngd.put("longitude", destino.longitude)

    locationd.put("latLng", latLngd)

    destination.put("location", locationd)
    jsonObject.put("destination", destination)

    /*jsonObject.put("travelMode", "DRIVE")
    jsonObject.put("computeAlternativeRoutes", false)
    jsonObject.put("units", "IMPERIAL")*/

    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()

    Log.d("MandarJSONruta",jsonObjectString)

    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    CoroutineScope(Dispatchers.IO).launch {
        // Hacer el request POST y obtener respuesta
        val response = service.getRuta(requestBody)
        Log.d("TU mama", response.toString())
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
                mUser.routes.forEach { ruta ->
                    if (ruta != null) {
                        recuperarResultados.add(RutaInfo(distancia = ruta.distance, duracion = ruta.duration, polilinea = ruta.polyline.encodPolyline))
                        Log.d("Polilineaaaa", ruta.polyline.encodPolyline)
                    } else {
                        //manejar error no se eonctraron resultados
                    }
                }


            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
    }
}