package com.example.trovare.ui.theme.Pantallas.Itinerarios

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.trovare.Data.Lugar
import com.example.trovare.Data.NearbyLocationsClass
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Mapa.APIServiceBuscarUbicacionesCercanas
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador
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


fun obtenerMarcadorItinerario(
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

            viewModel.setDestinoItinerario(place.latLng?: LatLng(viewModel.estadoMapaItinerario.value.destino.latitude, viewModel.estadoMapaItinerario.value.destino.longitude))
            viewModel.setNombreMapaItinerario(place.name?:"")
            viewModel.setRatingMapaItinerario(place.rating)
            viewModel.setIdMapaItinerario(place.id?:"")
            viewModel.setUbicacionMapaItinerario(place.latLng?: LatLng(viewModel.estadoMapaItinerario.value.destino.latitude, viewModel.estadoMapaItinerario.value.destino.longitude))

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
                        viewModel.setImagenMapaItinerario(imagenBitmap)
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("testLugar", "Place not found: " + exception.message)
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }
            }

            viewModel.setMarcadorInicializadoItinerario(true)
            viewModel.setInformacionInicializadaItinerario(true)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("testLugar", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}

fun obtenerMarcadorEntreMuchosItinerario(
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

            viewModel.setNombreMapaItinerario(place.name?:"")
            viewModel.setRatingMapaItinerario(place.rating?:-1.0)
            viewModel.setIdMapaItinerario(place.id?:"")

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

                        viewModel.setImagenMapaItinerario(imagenBitmap)
                        viewModel.setInformacionInicializadaItinerario(true)

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


interface APIServiceBuscarUbicacionesCercanasItinerario {
    @Headers(
        "Content-Type: application/json",
        "X-Goog-Api-Key: AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc",
        "X-Goog-FieldMask: places.location,places.id",
    )
    @POST("/v1/places:searchNearby")
    suspend fun createPlaceNearbyLocation(@Body requestBody: RequestBody): Response<ResponseBody>//cambiar import de response?
}


fun rawJSONUbicacionesCercanasItinerario(
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
    val service = retrofit.create(APIServiceBuscarUbicacionesCercanasItinerario::class.java)

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

                viewModel.estadoMapaItinerario.value.marcadores.clear()

                mUser.nearbyLocations.forEach { lugar ->
                    if (lugar != null) {
                        viewModel.estadoMapaItinerario.value.marcadores.add(Marcador(ubicacion = LatLng(lugar.location.latitude, lugar.location.longitude), id = lugar.id))
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

fun agregarLugarAlItinerario(
    id: String,
    nombreLugar: String,
    ubicacion: LatLng,
    viewModel: TrovareViewModel
) {
    val lugarNuevo = Lugar(
        id = id,
        nombreLugar =  nombreLugar,
        fechaDeVisita = null,
        horaDeVisita = null,
        origen = null,
        ubicacion = ubicacion,
        ruta = null,
        zoom = 15f,
        imagen = viewModel.estadoMapaItinerario.value.imagen
    )
    val itinerarioActualValor = viewModel.itinerarioActual.value

    // Verificar si la lista de lugares existe, si no, crearla
    if (itinerarioActualValor.lugares == null) {
        itinerarioActualValor.lugares = mutableListOf()
    }
    // Agregar el nuevo lugar a la lista de lugares del itinerario actual
    itinerarioActualValor.lugares?.add(lugarNuevo)
    // Actualizar el valor del itinerario actual en MutableStateFlow
    //viewModel._itinerarioActual.value = itinerarioActualValor
}



/*
package com.example.trovare.ui.theme.Pantallas.Itinerarios

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FilterListOff
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trovare.Api.rawJSON
import com.example.trovare.Data.Places
import com.example.trovare.Data.categorias
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.example.trovare.ui.theme.Pantallas.Mapa.MapStyle
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun AgregarLugarItinerario(
    modifier: Modifier = Modifier,
    state: MapState,
    navController: NavController,
    viewModel: TrovareViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient,
    placesClient: PlacesClient
){

    LaunchedEffect(key1 = Unit){
        viewModel.getLastLocation(fusedLocationProviderClient)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
    ){
        //Variables-------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------

        //Busqueda---------------------------
        var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
        var job: Job? by remember { mutableStateOf(null) }
        val prediccionesBusquedaMapa by remember { mutableStateOf(mutableStateListOf<Places>()) }//lista de lugares
        var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//
        var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
            mutableStateOf(TextFieldValue("", TextRange(0, 7)))
        }

        val padding = it//guardar padding, no se utiliza pero no se puede borrar:(((((
        fun iniciarTimer() {
            job = CoroutineScope(Dispatchers.Default).launch {

                busquedaEnProgreso = true

                while (tiempoRestante > 0) {
                    delay(1000)
                    tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
                }

                busquedaEnProgreso = false

                rawJSON(
                    query = textoBuscar.text,
                    recuperarResultados = prediccionesBusquedaMapa
                )
            }
        }

        //Mapa-------------------------------
        val estadoMapa by viewModel.estadoMapa.collectAsState()
        var zoom by remember { mutableFloatStateOf(15f) }


        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(estadoMapa.origen, 15f)
        }
        val mapProperties = MapProperties(
            // Only enable if user has accepted location permissions.
            isMyLocationEnabled = state.lastKnownLocation != null,
            mapStyleOptions = MapStyleOptions(MapStyle.json)
        )

        //Filtros----------------------------
        var filtroExtendido by rememberSaveable { mutableStateOf(false) }
        val marcadores by remember { mutableStateOf(mutableListOf<Marcador>()) }

        //UI--------------------------------------------------------------------------------------------
        //----------------------------------------------------------------------------------------------

        Box(modifier = modifier
            .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ){
            //Mapa--------------------------------------------------------------------------------------
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                properties = mapProperties,
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(mapToolbarEnabled = false)
            ) {
                //Si cambia la ubicacion del usuario,
                MapEffect(estadoMapa.origen) {
                    val cameraPosition = CameraPosition.fromLatLngZoom(estadoMapa.origen, zoom)
                    cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                }
                //Si cambia la ubicacion del destino,
                MapEffect(estadoMapa.destino) {
                    val cameraPosition = CameraPosition.fromLatLngZoom(estadoMapa.destino, zoom)
                    cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                }

                if(estadoMapa.marcadorInicializado){
                    MarkerInfoWindow(
                        state = rememberMarkerState(position = estadoMapa.destino),
                        snippet = "Some stuff",
                        onClick = {
                            Log.e("pruebaclick", "pruebaclick")
                            false
                        },
                        draggable = true
                    )
                }
                //varios marcadores
                if(estadoMapa.marcadoresInicializado){
                    marcadores.forEach { marcador ->
                        Marker(
                            state = rememberMarkerState(position = marcador.ubicacion),
                            onClick ={
                                viewModel.setDestino(marcador.ubicacion)
                                viewModel.setInformacionInicializada(false)
                                /*
                                viewModel.obtenerMarcadorEntreMuchos(
                                    placesClient = placesClient,
                                    placeId = marcador.id,
                                )

                                 */
                                //viewModel.set
                                false

                            }
                        )
                    }
                }
            }

            Column {
                //Busqueda------------------------------------------------------------------------------
                Card(
                    modifier = modifier
                        .padding(start = 55.dp, top = 15.dp, end = 55.dp, bottom = 5.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.Black),
                    border = CardDefaults.outlinedCardBorder()
                ){
                    TextField(
                        modifier = modifier
                            .fillMaxWidth(),
                        value = textoBuscar,
                        onValueChange = {
                            textoBuscar = it
                            job?.cancel() // Cancela la corrutina actual si es que existe
                            tiempoRestante = 1//resetea el timer a 1 segundo
                            iniciarTimer()//reinicia la cuenta regresiva del timer
                        },
                        leadingIcon = {
                            if(busquedaEnProgreso){
                                CircularProgressIndicator(
                                    modifier = modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Rounded.TravelExplore,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { /*TODO*/ },
                                colors = IconButtonDefaults.iconButtonColors(

                                )
                            ){
                                Icon(imageVector = Icons.Rounded.FilterList, contentDescription = "")
                            }
                            IconToggleButton(
                                checked = filtroExtendido,
                                onCheckedChange = { checked -> filtroExtendido = checked },
                                colors = IconButtonDefaults.iconToggleButtonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White,
                                    checkedContentColor = Color.White
                                )
                            ) {
                                if(filtroExtendido){
                                    Icon(
                                        imageVector = Icons.Rounded.FilterListOff,
                                        contentDescription = ""
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.FilterList,
                                        contentDescription = ""
                                    )
                                }

                            }
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        placeholder = { Text(text = "Buscar lugares", style = MaterialTheme.typography.labelSmall) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedContainerColor = Color.Black,
                            focusedContainerColor = Color.Black,
                            cursorColor = Color.White,
                        ),
                    )
                }
                //Mostrar filtros-----------------------------------------------------------------------
                if(filtroExtendido){
                    LazyRow(
                        modifier = modifier
                            .padding(horizontal = 55.dp)
                    ){
                        items(categorias){categoria ->
                            Card(
                                modifier = modifier
                                    .padding(end = 5.dp)
                                    .clickable {
                                        zoom = 13f
                                        viewModel.reiniciarImagen()
                                        viewModel.setPolilineaInicializada(false)
                                        viewModel.setMarcadorInicializado(false)
                                        viewModel.setMarcadoresInicializado(false)
                                        viewModel.setInformacionInicializada(false)
                                        viewModel.getLastLocation(fusedLocationProviderClient = fusedLocationProviderClient)
                                        CoroutineScope(Dispatchers.Default).launch {
                                            delay(200)
                                            /*
                                            rawJSONUbicacionesCercanas(
                                                filtro = categoria.nombre,
                                                recuperarResultados = marcadores,
                                                viewModel = viewModel,
                                                ubicacion = estadoMapa.origen
                                            )

                                             */
                                        }

                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                )
                            ){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Icon(
                                        modifier = modifier.padding(5.dp),
                                        imageVector = categoria.icono,
                                        contentDescription = ""
                                    )
                                    Text(
                                        modifier = modifier.padding(end = 5.dp),
                                        text = categoria.nombre,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }

                        }
                    }
                }
                //Mostrar resultados de la Busqueda-----------------------------------------------------
                if(!busquedaEnProgreso && textoBuscar.text != ""){
                    if(prediccionesBusquedaMapa.isNotEmpty()){
                        Card(
                            modifier = modifier
                                .padding(horizontal = 25.dp, vertical = 5.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Color.Black),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            LazyColumn(
                                modifier = modifier.padding(5.dp)
                            ){
                                items(prediccionesBusquedaMapa){lugar ->
                                    Box(
                                        modifier = modifier.clickable {
                                            zoom = 15f
                                            viewModel.reiniciarImagen()
                                            viewModel.setPolilineaInicializada(false)
                                            viewModel.setMarcadoresInicializado(false)
                                            viewModel.setMarcadorInicializado(false)
                                            viewModel.setInformacionInicializada(false)
                                            viewModel.obtenerMarcador1(
                                                placesClient = placesClient,
                                                placeId = lugar.id,
                                            )
                                            textoBuscar = TextFieldValue("")
                                        }
                                    ){
                                        Column(
                                            modifier = modifier.padding(5.dp)
                                        ) {
                                            Text(
                                                modifier = modifier.padding(3.dp),
                                                text = lugar.displayName?.text?: "",
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                modifier = modifier.padding(3.dp),
                                                text = lugar.formattedAddress?: "",
                                                style = MaterialTheme.typography.labelSmall,
                                                textAlign = TextAlign.Justify,
                                                color = Color.White
                                            )
                                            Divisor2()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //Tarjeta informacion del lugar---------------------------------------------------------
                if(estadoMapa.informacionInicializada){
                    Box(
                        modifier = modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        Card(
                            modifier = modifier
                                .padding(horizontal = 25.dp, vertical = 10.dp)
                                .size(height = 100.dp, width = 270.dp)
                                .clickable {
                                    navController.navigate(Pantalla.Detalles.conArgs(estadoMapa.idLugar))
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Trv1,
                                contentColor = Color.White
                            )
                        ) {
                            Row {
                                Card(
                                    modifier = modifier
                                        .padding(5.dp)
                                        .aspectRatio(1f),
                                ) {
                                    val imagen = viewModel.imagen.value

                                    if (imagen != null) {
                                        Image(
                                            bitmap = imagen,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            contentScale = ContentScale.FillBounds
                                        )
                                    } else {
                                        Image(
                                            modifier = modifier
                                                .fillMaxSize(),
                                            painter = painterResource(id = R.drawable.image_placeholder),
                                            contentDescription = ""
                                        )
                                    }
                                }
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ){
                                    Text(
                                        text = estadoMapa.nombreLugar,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 2
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${estadoMapa.ratingLugar}/5",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = "",
                                            tint = Color.Yellow
                                        )
                                    }
                                    TextButton(
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .padding(end = 5.dp, bottom = 5.dp),
                                        onClick = {
                                            viewModel.agregarLugarALItinerario(id = estadoMapa.idLugar, nombreLugar = estadoMapa.nombreLugar, ubicacion = estadoMapa.ubicacionLugar)
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Guardado en el itinerario",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                            viewModel.setInformacionInicializada(false)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Trv3
                                        )
                                    ) {
                                        Text(
                                            text = "Agregar",
                                            color = Color.Black,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                job?.cancel() // Asegura que la corrutina se cancele al salir del composable
            }
        }
    }
}
 */