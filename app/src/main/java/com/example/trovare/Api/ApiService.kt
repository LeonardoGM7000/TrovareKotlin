package com.example.trovare.Api

import android.util.Log
import com.example.trovare.Data.Places
import com.example.trovare.Data.PlacesClass
import com.example.trovare.Data.PlacesNearbyClass
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
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


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
                var mUser = gson1.fromJson(prettyJson, PlacesClass::class.java)
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
data class Circle(
    val center: Center,
    val radius: Double
)
data class Center(
    val latitude: Double,
    val longitude: Double,
)



interface APIServiceBuscarPorUbicacion {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ",
        "X-Goog-FieldMask: places.displayName,places.id",

    )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearby(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun rawJSONLugarCercano() {


    // Crear Retrofit
    val retrofit = Retrofit.Builder()
        .baseUrl("https://places.googleapis.com")
        .build()

    // Crear Servicio
    val service = retrofit.create(APIServiceBuscarPorUbicacion::class.java)

    // Crear JSON usando JSONObject

    val center = JSONObject()
    center.put("latitude", 19.504507)
    center.put("longitude", -99.147314)

    val circle = JSONObject()
    circle.put("center", center)
    circle.put("radius", 500.0)

    val jsonObject = JSONObject()
    jsonObject.put("includedTypes", circle)
    jsonObject.put("maxResultCount", 5)
    jsonObject.put("locationRestriction", circle)



    // Convertir JSONObject a String
    val jsonObjectString = jsonObject.toString()

    // Crear RequestBody ()
    val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

    //recuperarResultados.clear()

    CoroutineScope(Dispatchers.IO).launch {
        // Hacer el request POST y obtener respuesta
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
                var mUser = gson1.fromJson(prettyJson, PlacesNearbyClass::class.java)
                mUser.placesNearby.forEach { lugar ->
                    if (lugar != null) {
                        //recuperarResultados.add(Places(id = lugar.id, formattedAddress = lugar.formattedAddress, displayName = lugar.displayName))
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