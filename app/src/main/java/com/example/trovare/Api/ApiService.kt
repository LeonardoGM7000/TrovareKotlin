package com.example.trovare.Api

import android.util.Log
import com.example.trovare.Data.NearbyLocationsClass
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.NearbyPlacesClass
import com.example.trovare.Data.Places
import com.example.trovare.Data.PlacesClass
import com.example.trovare.Data.Routes
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador
import com.example.trovare.ui.theme.Pantallas.Mapa.RutaInfo
import com.example.trovare.ui.theme.Pantallas.Resena
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
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


interface APIServiceBuscarTexto {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc",
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






fun obtenerResenas(
    placeId: String,
    recuperarResultados: MutableList<Resena>
) {
    val url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=$placeId&key=AIzaSyDJBAeLUu6KewjD9hhDGNP8gCnshpG5y7c&language=es"

    val cliente = OkHttpClient()
    val solicitud = Request.Builder().url(url).build()

    CoroutineScope(Dispatchers.IO).launch {
        cliente.newCall(solicitud).execute().use { respuesta ->
            if (respuesta.isSuccessful) {
                val cuerpoRespuesta = respuesta.body?.string()
                val datos = JSONObject(cuerpoRespuesta)

                if (datos.has("result")) {
                    val resenas = datos.getJSONObject("result").optJSONArray("reviews")

                    if (resenas != null) {

                        for (i in 0 until resenas.length()) {
                            val resena = resenas.getJSONObject(i)
                            val usuario = resena.optString("author_name", "Desconocido")
                            val puntuacion = resena.optInt("rating", -1)
                            val texto = resena.optString("text", "N/A")
                            val tiempo = resena.optInt("time",-1)
                            val fotoDePerfil = resena.optString("profile_photo_url","N/A")

                            recuperarResultados.add(Resena(usuario, puntuacion, texto, tiempo, fotoDePerfil))
                        }

                    } else {
                        Log.i("resena", "No se encontraron reseñas para este lugar.")
                    }
                } else {
                    Log.i("resena","No se encontraron detalles para el lugar.")
                }
            } else {
                Log.i("resena","Error en la solicitud: ${respuesta.message}")
            }
        }
    }
}