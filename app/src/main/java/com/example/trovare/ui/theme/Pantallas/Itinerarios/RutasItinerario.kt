package com.example.trovare.ui.theme.Pantallas.Itinerarios

import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.example.trovare.ui.theme.Pantallas.Mapa.MapStyle
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Api.obtenerResenas
import com.example.trovare.Api.rawJSON
import com.example.trovare.Api.rawJSONRutas
import com.example.trovare.Api.rawJSONUriFotos
import com.example.trovare.Api.rawJSONVariasFotos
import com.example.trovare.Data.Places
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Resena
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv10
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun RutasItinerario(
    modifier: Modifier = Modifier,
    state: MapState,
    navController: NavController,
    viewModel: TrovareViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient,
    placesClient: PlacesClient
){
    //Variables-------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    //Itinerario-------------------------
    val indiceActual by viewModel.indiceActual.collectAsState()

    //Busqueda---------------------------
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
    var job: Job? by remember { mutableStateOf(null) }
    val prediccionesBusquedaMapa by remember { mutableStateOf(mutableStateListOf<Places>()) }//lista de lugares
    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    val nombresFotos by remember { mutableStateOf(mutableStateListOf<String>()) }
    val ubicacionActual by viewModel.ubicacionActual.collectAsState()

    val colores = FilterChipDefaults.filterChipColors(
        iconColor = Color.White,
        selectedLeadingIconColor = Color.Black,
        labelColor = Color.White,
        selectedLabelColor = Color.Black,
        selectedContainerColor = Trv10,
        containerColor = Trv1
    )


    val estadoMapaRuta by viewModel.estadoMapaRuta.collectAsState()



    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.zoomRuta)
    }
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
        mapStyleOptions = MapStyleOptions(MapStyle.json)
    )


    //bottom sheet
    val scaffoldState = rememberBottomSheetScaffoldState()
    var peekHeight by remember { mutableStateOf(0.dp) }
    val scope = rememberCoroutineScope()

    //Mensajes
    val snackbarHostState = remember { SnackbarHostState() }

    //Funciones
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



    fun calcularZoom(punto1: LatLng, punto2: LatLng) {
        val latDiff = punto2.latitude - punto1.latitude
        val lonDiff = punto2.longitude - punto1.longitude

        val distancia = Math.sqrt(latDiff.pow(2) + lonDiff.pow(2))
        Log.d("distancia" ,"${distancia}")

        when {
            distancia > 10 -> {
                viewModel.setZoomRuta(5f)
            }
            distancia > 6 -> {
                viewModel.setZoomRuta(6f)
            }
            distancia > 2 -> {
                viewModel.setZoomRuta(7f)
            }
            distancia > 1 -> {
                viewModel.setZoomRuta(8f)
            }
            distancia > 0.5 -> {
                viewModel.setZoomRuta(9f)
            }
            distancia > 0.3 -> {
                viewModel.setZoomRuta(10f)
            }
            distancia > 0.15 -> {
                viewModel.setZoomRuta(11f)
            }
            distancia > 0.085 -> {
                viewModel.setZoomRuta(12f)
            }
            distancia > 0.04 -> {
                viewModel.setZoomRuta(13f)
            }
            distancia > 0.0 -> {
                viewModel.setZoomRuta(14f)
            }
        }
    }

    var nombre by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var numeroTelefono by rememberSaveable { mutableStateOf("") }
    var paginaWeb by rememberSaveable { mutableStateOf("") }
    var calificacion by rememberSaveable { mutableStateOf(-1.0) }



    LaunchedEffect(key1 = Unit){

        rawJSONVariasFotos(
            placeid = estadoMapaRuta.idLugarRuta,
            recuperarResultados = nombresFotos,
            viewModel = viewModel
        )

        viewModel.getLastLocation(fusedLocationProviderClient)
        if(estadoMapaRuta.polilineaCodRuta == ""){
            viewModel.setOrigenRuta(ubicacionActual)
        }

        viewModel.obtenerLugarRuta(
            placesClient = placesClient,
            placeId = estadoMapaRuta.idLugarRuta,
            nombre = { nombre = it?:"" },
            direccion = { direccion = it?:""},
            rating =  { calificacion = it?: -1.0},
            numeroTelefono = {numeroTelefono = it?: ""},
            paginaWeb = {paginaWeb = it?: ""},
        )
        calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
        when {
            estadoMapaRuta.transporteRuta == "auto" -> {
                scope.launch {
                    peekHeight = 100.dp
                }
                viewModel.setTransporteRuta("auto")
                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                rawJSONRutas(
                    origen = estadoMapaRuta.origenRuta,
                    destino = estadoMapaRuta.destinoRuta,
                    viewModel = viewModel,
                    //recuperarResultados = rutaInfo
                )
            }
            estadoMapaRuta.transporteRuta == "transporte" -> {
                scope.launch {
                    peekHeight = 100.dp
                }
                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                rawJSONRutas(
                    origen = estadoMapaRuta.origenRuta,
                    destino = estadoMapaRuta.destinoRuta,
                    viewModel = viewModel,
                    travel_mode = "TRANSIT"
                    //recuperarResultados = rutaInfo
                )
            }
            estadoMapaRuta.transporteRuta == "caminando" -> {
                scope.launch {
                    peekHeight = 100.dp
                }
                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                rawJSONRutas(
                    origen = estadoMapaRuta.origenRuta,
                    destino = estadoMapaRuta.destinoRuta,
                    viewModel = viewModel,
                    travel_mode = "WALK"
                    //recuperarResultados = rutaInfo
                )
            }
        }
    }

    //UI--------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        containerColor = Trv1,
        sheetPeekHeight = peekHeight,
        //Tarjeta superior para seleccionar lugar de origen-----------------------------------------
        topBar = {
            Surface(
                modifier = modifier
                    .wrapContentSize(),
                color = Trv1
            ) {
                Column(
                    modifier = modifier.fillMaxWidth(),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = modifier.padding(horizontal = 5.dp))
                        IconButton(
                            onClick = {navController.popBackStack()}
                        ){
                            Icon(
                                imageVector = Icons.Rounded.ArrowBackIos,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = modifier.padding(horizontal = 3.dp))
                        //icono de ubicación-------------------
                        if(busquedaEnProgreso){
                            CircularProgressIndicator(
                                modifier = modifier.size(20.dp),
                                color = Color.White
                            )
                        }else{
                            Icon(
                                modifier = modifier
                                    .size(20.dp),
                                imageVector = Icons.Rounded.MyLocation,
                                contentDescription = "",
                                tint = Color.White
                            )
                            //barra de búsqueda
                        }
                        Card(
                            modifier = modifier
                                .size(250.dp, 70.dp)
                                .padding(vertical = 10.dp, horizontal = 10.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Trv1),
                            border = CardDefaults.outlinedCardBorder()
                        ){
                            TextField(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .size(250.dp, 70.dp),
                                value = textoBuscar,
                                onValueChange = {
                                    textoBuscar = it
                                    job?.cancel() // Cancela la corrutina actual si es que existe
                                    tiempoRestante = 1//resetea el timer a 1 segundo
                                    iniciarTimer()//reinicia la cuenta regresiva del timer
                                },
                                textStyle = MaterialTheme.typography.labelSmall,
                                placeholder = {
                                    Text(
                                        text = "Tu ubicación",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    unfocusedContainerColor = Trv1,
                                    focusedTextColor = Color.White,
                                    focusedContainerColor = Trv1,
                                    cursorColor = Color.White
                                ),
                            )
                        }
                        //Ubicación destino
                    }
                    Row(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            modifier = modifier
                                .size(20.dp),
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Card(
                            modifier = modifier
                                .size(250.dp, 50.dp)
                                .padding(horizontal = 10.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Trv1),
                            border = CardDefaults.outlinedCardBorder()
                        ){
                            TextField(
                                enabled = false,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .size(250.dp, 70.dp),
                                value = "",
                                onValueChange = {},
                                textStyle = MaterialTheme.typography.labelSmall,
                                placeholder = {
                                    Text(
                                        text = estadoMapaRuta.nombreLugarRuta,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    disabledContainerColor = Trv1,
                                    focusedTextColor = Color.White,
                                    focusedContainerColor = Trv1,
                                    cursorColor = Color.White
                                ),
                            )
                        }
                    }
                    Row(
                        modifier = modifier.align(Alignment.CenterHorizontally)
                    ) {
                        //Viajar en Carro-----------------------------------------------------------
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsCar,
                                    contentDescription = ""
                                )
                            },
                            selected = estadoMapaRuta.transporteRuta == "auto",
                            onClick = {
                                viewModel.setPolilineaInicializadaRuta(false)
                                scope.launch {
                                    peekHeight = 100.dp
                                }
                                viewModel.setTransporteRuta("auto")
                                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                                rawJSONRutas(
                                    origen = estadoMapaRuta.origenRuta,
                                    destino = estadoMapaRuta.destinoRuta,
                                    viewModel = viewModel,
                                    //recuperarResultados = rutaInfo
                                )
                            },
                            colors = colores,
                            label = { Text(text = "auto") }
                        )
                        //Viajar en Transporte------------------------------------------------------
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsBus,
                                    contentDescription = "",
                                )
                            },
                            selected = estadoMapaRuta.transporteRuta == "transporte",
                            onClick = {
                                viewModel.setPolilineaInicializadaRuta(false)
                                scope.launch {
                                    peekHeight = 100.dp
                                }
                                viewModel.setTransporteRuta("transporte")
                                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                                rawJSONRutas(
                                    origen = estadoMapaRuta.origenRuta,
                                    destino = estadoMapaRuta.destinoRuta,
                                    viewModel = viewModel,
                                    travel_mode = "TRANSIT"
                                    //recuperarResultados = rutaInfo
                                )
                            },
                            colors = colores,
                            label = { Text(text = "transporte") }
                        )
                        //Viajar Caminando-----------------------------------------------------------
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsWalk,
                                    contentDescription = "",
                                )
                            },
                            selected = estadoMapaRuta.transporteRuta == "caminando",
                            onClick = {
                                viewModel.setPolilineaInicializadaRuta(false)
                                scope.launch {
                                    peekHeight = 100.dp
                                }
                                viewModel.setTransporteRuta("caminando")
                                calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                                rawJSONRutas(
                                    origen = estadoMapaRuta.origenRuta,
                                    destino = estadoMapaRuta.destinoRuta,
                                    viewModel = viewModel,
                                    travel_mode = "WALK"
                                    //recuperarResultados = rutaInfo
                                )
                            },
                            colors = colores,
                            label = { Text(text = "caminando") }
                        )
                    }
                    //Auto
                }
                //Busqueda------------------------------------------------------------------------------
            }
        },
        //Contenido de la tarjeta inferior----------------------------------------------------------
        sheetContent = {
            // Sheet content
            val distancia = (estadoMapaRuta.distanciaEntrePuntos/1000).toInt()
            val tiempoDeViajeTemp = estadoMapaRuta.tiempoDeViaje.dropLast(1).toIntOrNull()

            Column(
                modifier = modifier.padding(horizontal = 25.dp)
            ) {
                //Mostrar icono de transporte seleccionado
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = modifier.fillMaxWidth(0.7f)){
                       Row(
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           Icon(
                               modifier = modifier.size(35.dp),
                               imageVector =
                               when {
                                   estadoMapaRuta.transporteRuta == "caminando" -> {
                                       Icons.Rounded.DirectionsWalk
                                   }
                                   estadoMapaRuta.transporteRuta == "transporte" -> {
                                       Icons.Rounded.DirectionsBus
                                   }
                                   else -> {
                                       Icons.Rounded.DirectionsCar
                                   }
                               },
                               contentDescription = ""
                           )
                           Spacer(modifier = modifier.padding(horizontal = 3.dp))
                           if(tiempoDeViajeTemp != null){
                               if(((tiempoDeViajeTemp/60)/60)!=0){
                                   Text(
                                       text = "${((tiempoDeViajeTemp/60)/60)} hrs ",
                                       style = MaterialTheme.typography.labelMedium
                                   )
                               }
                               Text(
                                   text = "${tiempoDeViajeTemp%60} min",
                                   style = MaterialTheme.typography.labelMedium
                               )
                           }
                           Spacer(modifier = modifier.padding(horizontal = 3.dp))
                           Text(
                               text = "(${distancia} km)",
                               style = MaterialTheme.typography.labelMedium
                           )
                       }
                    }
                    //Boton agregar ruta-----
                    TextButton(
                        enabled = estadoMapaRuta.polilineaInicializadaRuta,
                        onClick = {
                            scope.launch {
                                scaffoldState.bottomSheetState.show()
                                snackbarHostState.showSnackbar(
                                    message = "Ruta guardada",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            viewModel.guardarOrigenRuta(indiceActual = indiceActual, origenNuevo = estadoMapaRuta.origenRuta)
                            viewModel.guardarRutaLugar(indiceActual = indiceActual, rutaNueva = estadoMapaRuta.polilineaCodRuta)
                            viewModel.guardarZoomLugar(indiceActual = indiceActual, nuevoZoom = estadoMapaRuta.zoomRuta)
                            viewModel.guardarTransporte(indiceActual = indiceActual, nuevoTransporte = estadoMapaRuta.transporteRuta)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Trv10,
                            contentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = "Agregar ruta"
                        )
                    }
                }
                Divider(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 10.dp),
                    color = Color.White
                )
                //Mostrar imágenes del lugar
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 5.dp)
                ){

                    if(estadoMapaRuta.imgsInicializadas /*&& estadoMapaRuta.marcadorInicializadoRuta*/){
                        Log.d("¿Hay nombre?", nombresFotos.toString())
                    }
                    //viewModel.setImgsInicializadas(false)
                    /*val fotoUri = mutableListOf<String>()
                    rawJSONUriFotos(
                        photoName = nombresFotos.first(),
                        recuperarResultados = fotoUri
                    )*/


                    item{
                        Card(
                            modifier = modifier
                                .padding(end = 5.dp)
                                .size(150.dp)
                                .aspectRatio(1F),
                        ){
                            Box(
                                modifier = modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    item{
                        Card(
                            modifier = modifier
                                .padding(end = 5.dp)
                                .size(150.dp)
                                .aspectRatio(1F),
                        ){
                            Box(
                                modifier = modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    item{
                        Card(
                            modifier = modifier
                                .padding(end = 5.dp)
                                .size(150.dp)
                                .aspectRatio(1F),
                        ){
                            Box(
                                modifier = modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomEnd
                            ){
                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = modifier.padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(0.7f),
                        text = nombre,
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                    Row( modifier = modifier
                        .fillMaxWidth(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier.fillMaxWidth(0.75f),
                            text = "${calificacion}/5",
                            textAlign = TextAlign.Right,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Color.Yellow
                        )
                    }
                }


                //Direccion-----------------------------------------------------------------------------
                if(direccion != ""){
                    Row(modifier = modifier
                        .padding(vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            text = direccion,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                //Horario de apertura-------------------------------------------------------------------
                if(numeroTelefono != ""){
                    Row(modifier = modifier
                        .padding(vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            text = numeroTelefono,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                //Pagina web----------------------------------------------------------------------------
                if(paginaWeb != ""){
                    Row(modifier = modifier
                        .padding(vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.Web,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            //modifier = modifier.clickable { context.startActivity(intent) },
                            text = paginaWeb,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        },
        //Mensajes------------
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },

    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {

            Box(modifier = modifier
                .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ){
                //Mapa--------------------------------------------------------------------------------------
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    properties = mapProperties,
                    cameraPositionState = cameraPositionState,
                    uiSettings = MapUiSettings(
                        compassEnabled = false,
                        mapToolbarEnabled = true,
                        myLocationButtonEnabled = false,
                        rotationGesturesEnabled = false,
                        tiltGesturesEnabled = true,
                        zoomControlsEnabled = false
                    ),
                ) {
                    MapEffect(estadoMapaRuta.origenRuta) {
                        calcularZoom(estadoMapaRuta.origenRuta, estadoMapaRuta.destinoRuta)
                    }
                    MapEffect(estadoMapaRuta.zoomRuta){
                        val cameraPosition = CameraPosition.fromLatLngZoom(
                            LatLng(
                                ((estadoMapaRuta.origenRuta.latitude+estadoMapaRuta.destinoRuta.latitude)/2),
                                ((estadoMapaRuta.origenRuta.longitude+estadoMapaRuta.destinoRuta.longitude)/2)),
                            estadoMapaRuta.zoomRuta
                        )
                        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                    }

                    //Mostrar marcador de origen
                    if(estadoMapaRuta.marcadorInicializadoRuta){
                        MarkerInfoWindow(
                            state = rememberMarkerState(position = estadoMapaRuta.origenRuta),
                            onClick = {
                                false
                            },
                            draggable = false
                        )
                    }
                    //Mostrar marcador de destino(siempre se muestra)
                    MarkerInfoWindow(
                        state = rememberMarkerState(position = estadoMapaRuta.destinoRuta),
                        onClick = {
                            false
                        },
                        draggable = false
                    )
                    if(estadoMapaRuta.polilineaInicializadaRuta){

                        MapEffect(estadoMapaRuta.origenRuta) {
                            val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(((estadoMapaRuta.origenRuta.latitude+estadoMapaRuta.destinoRuta.latitude)/2),((estadoMapaRuta.origenRuta.longitude+estadoMapaRuta.destinoRuta.longitude)/2)), estadoMapaRuta.zoomRuta)
                            cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                        }

                        val encodedPolyline = estadoMapaRuta.polilineaCodRuta // Reemplaza con tu encoded polyline
                        val decodedPolyline: List<LatLng> = PolyUtil.decode(encodedPolyline)

                        for (latLng in decodedPolyline) {
                            Log.d("ListadeLats", "Latitud: ${latLng.latitude}, Longitud: ${latLng.longitude}")
                        }
                        Polyline(
                            points = decodedPolyline,
                            width = 5f,
                            color = Trv10
                        )
                    }
                }

                Column {

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
                                                viewModel.setZoomRuta(15f)
                                                viewModel.setPolilineaInicializadaRuta(false)
                                                viewModel.setMarcadorInicializadoRuta(false)
                                                viewModel.setTransporteRuta("")
                                                viewModel.obtenerMarcadorOrigen(
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

