package com.example.trovare.ui.theme.Pantallas.Mapa

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.trovare.Api.apiRutasMapaPrincipal
import com.example.trovare.Api.rawJSON
import com.example.trovare.Api.rawJSONUbicacionesCercanas
import com.example.trovare.Data.Places
import com.example.trovare.Data.categorias
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv10
import com.example.trovare.ui.theme.Trv3
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
import java.lang.Math.sqrt
import kotlin.math.pow

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun MapaPrincipal(
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

    //Mapa-------------------------------
    val estadoMapa by viewModel.estadoMapa.collectAsState()


    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(estadoMapa.origen, estadoMapa.zoom)
    }
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
        mapStyleOptions = MapStyleOptions(MapStyle.json)
    )

    //Filtros----------------------------
    var filtroExtendido by rememberSaveable { mutableStateOf(false) }
    val marcadores by remember { mutableStateOf(mutableListOf<Marcador>()) }


    fun calcularZoom(punto1: LatLng, punto2: LatLng) {
        val latDiff = punto2.latitude - punto1.latitude
        val lonDiff = punto2.longitude - punto1.longitude

        val distancia = sqrt(latDiff.pow(2) + lonDiff.pow(2))
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
            //Si cambia la ubicacion,
            MapEffect(estadoMapa.destino) {
                val cameraPosition = CameraPosition.fromLatLngZoom(estadoMapa.destino, estadoMapa.zoom)
                cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
            }
            MapEffect(estadoMapa.origen) {
                val cameraPosition = CameraPosition.fromLatLngZoom(estadoMapa.origen, estadoMapa.zoom)
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
                            viewModel.obtenerMarcadorEntreMuchos(
                                placesClient = placesClient,
                                placeId = marcador.id,
                            )
                            false
                        }
                    )
                }
            }
            if(estadoMapa.polilineaInicializada){

                MapEffect(estadoMapa.origen) {
                    val cameraPosition = CameraPosition.fromLatLngZoom(LatLng(((estadoMapa.origen.latitude+estadoMapa.destino.latitude)/2),((estadoMapa.origen.longitude+estadoMapa.destino.longitude)/2)), estadoMapa.zoom)
                    cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                }

                val encodedPolyline = estadoMapa.polilineaCod // Reemplaza con tu encoded polyline
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
                                    viewModel.setZoom(13f)
                                    viewModel.reiniciarImagen()
                                    viewModel.setPolilineaInicializada(false)
                                    viewModel.setMarcadorInicializado(false)
                                    viewModel.setMarcadoresInicializado(false)
                                    viewModel.setInformacionInicializada(false)
                                    viewModel.getLastLocation(fusedLocationProviderClient = fusedLocationProviderClient)
                                    CoroutineScope(Dispatchers.Default).launch {
                                        delay(200)
                                        rawJSONUbicacionesCercanas(
                                            filtro = categoria.nombre,
                                            recuperarResultados = marcadores,
                                            viewModel = viewModel,
                                            ubicacion = estadoMapa.origen
                                        )
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
                                        viewModel.setZoom(15f)
                                        viewModel.reiniciarImagen()
                                        viewModel.setPolilineaInicializada(false)
                                        viewModel.setMarcadoresInicializado(false)
                                        viewModel.setMarcadorInicializado(false)
                                        viewModel.setInformacionInicializada(false)
                                        viewModel.obtenerMarcador(
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
                        colors = CardDefaults.cardColors(Trv1)
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

                                        //Log.d("Algun punto",puntosRuta[0].latitude.toString())

                                        val destino = LatLng(estadoMapa.destino.latitude, estadoMapa.destino.longitude)
                                        val origen = state.lastKnownLocation?.latitude?.let { state.lastKnownLocation?.longitude?.let { it1 -> LatLng(it, it1) } }
                                        calcularZoom(origen!!, destino)
                                        if (origen != null) {
                                            apiRutasMapaPrincipal(
                                                destino = destino,
                                                origen = origen,
                                                viewModel = viewModel,
                                            )
                                        }

                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Trv3
                                    )
                                ) {
                                    Text(
                                        text = "Ruta",
                                        color = Color.Black
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

