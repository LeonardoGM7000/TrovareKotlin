package com.example.trovare.Api

import android.util.Log
import com.example.trovare.Data.Routes
import com.example.trovare.ViewModel.TrovareViewModel
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
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIServiceSolicitaPolyline {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc",
        "X-Goog-FieldMask: routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline"
    )
    @POST("/directions/v2:computeRoutes")
    suspend fun getRuta(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}

fun apiRutasItinerario(
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

                viewModel.setPolilineaCodRuta(rutaInfo.polilinea)
                viewModel.setTiempoDeViajeRuta(rutaInfo.duracion)
                viewModel.setDistanciaEntrePuntosRuta(rutaInfo.distancia)
                viewModel.setPolilineaInicializadaRuta(true)

            } else {

                Log.e("RETROFIT_ERROR", response.code().toString())

            }
        }
    }
}