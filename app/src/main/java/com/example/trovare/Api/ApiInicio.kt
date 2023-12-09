package com.example.trovare.Api

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.NearbyPlacesClass
import com.example.trovare.ViewModel.TrovareViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
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



                    viewModel.obtenerImagenLugar(placeId = lugar!!.id, placesClient = placesClient, viewModel = viewModel)
                    //TODO HACERLA S[INCRONA
                    Log.d("terminar","ultimo")
                    val imagen = viewModel.imagen.value
                    listaLugaresCercanos.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType, imagen = imagen))



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
    viewModel: TrovareViewModel
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

    CoroutineScope(Dispatchers.IO).launch {
        //Hacer el request POST y obtener respuesta

        //TODO AGREGAR VERIFICAR LA CONEXION CON INTERNET

        val response = service.createPlaceNearby(requestBody)
        withContext(Dispatchers.Main) {
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
                    if (lugar != null) {
                        //listaLugaresPopulares.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType))
                    } else {
                        //manejar error no se eonctraron resultados
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
    viewModel: TrovareViewModel
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
                    if (lugar != null) {
                        //listaPuntosDeInteres.add(NearbyPlaces(id = lugar.id, displayName  = lugar.displayName, rating = lugar.rating, primaryType = lugar.primaryType))
                    } else {
                        //manejar error no se eonctraron resultados
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