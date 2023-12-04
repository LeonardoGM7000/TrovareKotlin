package com.example.trovare.ui.theme.Pantallas.Itinerarios

import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.example.trovare.ui.theme.Pantallas.Mapa.MapStyle
import com.example.trovare.ui.theme.Pantallas.Mapa.Marcador



import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.EditLocationAlt
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FilterListOff
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.MyLocation
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
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
import androidx.navigation.NavController
import com.example.trovare.Api.rawJSON
import com.example.trovare.Api.rawJSONRutas
import com.example.trovare.Api.rawJSONUbicacionesCercanas
import com.example.trovare.Data.Places
import com.example.trovare.Data.categorias
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Perfil.PerfilPrincipal
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv10
import com.example.trovare.ui.theme.Trv3
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.AdvancedMarker
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.PolyUtil
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.Polyline
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

    //Busqueda---------------------------
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
    var job: Job? by remember { mutableStateOf(null) }
    val prediccionesBusquedaMapa by remember { mutableStateOf(mutableStateListOf<Places>()) }//lista de lugares
    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
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
    val marcadorInicializado by viewModel.marcadorInicializado.collectAsState()
    val nombreLugar by viewModel.nombreLugar.collectAsState()
    var zoom by remember { mutableFloatStateOf(15f) }

    val origenRuta by viewModel.origenRuta.collectAsState()
    val destinoRuta by viewModel.destinoRuta.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(origenRuta, 15f)
    }
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
        mapStyleOptions = MapStyleOptions(MapStyle.json)
    )

    //Transporte seleccionado
    var transporte by remember { mutableStateOf("") }

    //Ruta--------------------------------

    val polilineaInicializadaRuta by viewModel.polilineaInicializadaRuta.collectAsState()
    val polilineaCod by viewModel.polilineaCod.collectAsState()




    //UI--------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    Scaffold(
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
                                contentDescription = ""
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
                                contentDescription = ""
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
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.White,
                                    containerColor = Trv1,
                                    cursorColor = Color.White,
                                )
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
                            contentDescription = ""
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
                                        text = "Ubicación destino",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.White,
                                    containerColor = Trv1,
                                    cursorColor = Color.White,
                                )
                            )
                        }
                    }
                    Row(
                        modifier = modifier.align(Alignment.CenterHorizontally)
                    ) {
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsCar,
                                    contentDescription = ""
                                )
                            },
                            selected = transporte == "auto",
                            onClick = { transporte = "auto" },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Trv10, containerColor = Trv1),
                            label = { Text(text = "auto") }
                        )
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsBus,
                                    contentDescription = ""
                                )
                            },
                            selected = transporte == "transporte",
                            onClick = { transporte = "transporte" },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Trv10, containerColor = Trv1),
                            label = { Text(text = "transporte") }
                        )
                        FilterChip(
                            modifier = modifier.padding(horizontal = 5.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.DirectionsWalk,
                                    contentDescription = ""
                                )
                            },
                            selected = transporte == "caminando",
                            onClick = { transporte = "caminando" },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Trv10, containerColor = Trv1),
                            label = { Text(text = "caminando") }
                        )
                    }
                    //Auto
                }
                //Busqueda------------------------------------------------------------------------------
            }
        },
    //Mapa para agregar la ruta al lugar del itinerario---------------------------------------------
    ) { it ->
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
                    uiSettings = MapUiSettings(mapToolbarEnabled = false)
                ) {
                    //Si cambia la ubicacion de origen,
                    MapEffect(origenRuta) {
                        val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(((origenRuta.latitude+destinoRuta.latitude)/2),((origenRuta.longitude+destinoRuta.longitude)/2)), zoom)
                        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                    }
                    MapEffect(destinoRuta) {
                        val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(((origenRuta.latitude+destinoRuta.latitude)/2),((origenRuta.longitude+destinoRuta.longitude)/2)), zoom)
                        cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                    }

                    //Mostrar marcador de origen
                    if(marcadorInicializado){
                        MarkerInfoWindow(
                            state = rememberMarkerState(position = origenRuta),
                            onClick = {
                                false
                            },
                            draggable = false
                        )
                    }

                    //Mostrar marcador de destino(siempre se muestra)
                    MarkerInfoWindow(
                        state = rememberMarkerState(position = destinoRuta),
                        onClick = {
                            false
                        },
                        draggable = false
                    )

                    if(polilineaInicializadaRuta){
                        val encodedPolyline = polilineaCod // Reemplaza con tu encoded polyline
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
                                                zoom = 15f
                                                viewModel.setPolilineaInicializadaRuta(false)
                                                viewModel.setMarcadorInicializado(false)
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
                    //Boton para agregar ruta-------------------------------------------------------
                    Box(
                        modifier = modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.BottomCenter
                    ){
                        TextButton(
                            enabled = transporte != "",
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 70.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Trv10,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                val origen = LatLng(destinoRuta.latitude, destinoRuta.longitude)
                                val destino = LatLng(origenRuta.latitude, origenRuta.longitude)
                                if (destino != null) {
                                    rawJSONRutas(
                                        origen = origen,
                                        destino = destino,
                                        viewModel = viewModel,
                                        //recuperarResultados = rutaInfo
                                    )
                                }
                            }
                        ) {
                            Text(text = "Agregar ruta")
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