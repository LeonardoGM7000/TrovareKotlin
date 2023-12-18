package com.example.trovare.ui.theme.Pantallas.Mapa

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Attractions
import androidx.compose.material.icons.rounded.DirectionsBus
import androidx.compose.material.icons.rounded.DirectionsCar
import androidx.compose.material.icons.rounded.DirectionsWalk
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FilterListOff
import androidx.compose.material.icons.rounded.Route
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Api.rawJSON
import com.example.trovare.Data.Places
import com.example.trovare.Data.categorias
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv10
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv7
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
fun MapaPrincipal(
    modifier: Modifier = Modifier,
    state: MapState,
    navController: NavController,
    viewModel: TrovareViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient,
    placesClient: PlacesClient
){

    LaunchedEffect(key1 = Unit){
        viewModel.getLastLocation(fusedLocationProviderClient)//obtener la última ubicación
        viewModel.setOrigen(viewModel.ubicacionActual.value)//actualizarla como origen del mapa principal

    }

    val colores = FilterChipDefaults.filterChipColors(
        iconColor = Color.White,
        selectedLeadingIconColor = Color.Black,
        labelColor = Color.White,
        selectedLabelColor = Color.Black,
        selectedContainerColor = Trv10,
        containerColor = Trv1
    )

    val sheetState = rememberModalBottomSheetState()

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


    var transporteSeleccionado by remember { mutableStateOf("") }
    val distancia = (estadoMapa.distanciaEntrePuntos/1000).toInt()
    val tiempoDeViajeTemp = estadoMapa.tiempoDeViaje.dropLast(1).toIntOrNull()


    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {

            busquedaEnProgreso = true

            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
            }

            rawJSON(
                query = textoBuscar.text,
                recuperarResultados = prediccionesBusquedaMapa
            )

            busquedaEnProgreso = false
        }
    }

    //UI--------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------


    //Bottom sheet con info del lugar---------------------------------------------------------------
    if(estadoMapa.informacionInicializada){
        ModalBottomSheet(
            shape = RectangleShape,
            onDismissRequest = { viewModel.setInformacionInicializada(false) },
            sheetState = sheetState,
            containerColor = Trv1
        ) {

            LazyColumn(
                modifier = modifier.padding(horizontal = 10.dp)
            ){
                item {
                    Row(
                        modifier = modifier
                            .clickable(
                                indication = null,
                                interactionSource = NoRippleInteractionSource()
                            ) { navController.navigate(Pantalla.Detalles.conArgs(estadoMapa.idLugar)) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Card(
                            modifier = modifier
                                .size(120.dp)
                                .aspectRatio(1f)
                        ){
                            if (estadoMapa.imagen != null) {
                                Image(
                                    bitmap = estadoMapa.imagen!!,
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
                            modifier = modifier.padding(start = 5.dp)
                        ) {
                            //Nombre del lugar----------------------------------------------------------------------
                            Text(
                                text = estadoMapa.nombreLugar,
                                textAlign = TextAlign.Justify,
                                style = MaterialTheme.typography.labelMedium,
                                maxLines = 2
                            )
                            //Icono y calificación del lugar--------------------------------------------------------
                            Row(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Attractions,
                                    contentDescription = ""
                                )
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Trv7)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = "",
                                            tint = Trv10
                                        )
                                        Text(
                                            modifier = modifier.padding(horizontal = 5.dp),
                                            text = if(estadoMapa.ratingLugar == null) "---" else estadoMapa.ratingLugar.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Trv10
                                        )
                                    }
                                }
                            }
                            //Informaci[on de ruta--------------------------------------------------
                            if(transporteSeleccionado != ""){
                                Row(
                                    modifier = modifier.padding(bottom = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = modifier.fillMaxWidth()){
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector =
                                                when {
                                                    transporteSeleccionado == "auto" -> {
                                                        Icons.Rounded.DirectionsCar
                                                    }
                                                    transporteSeleccionado == "caminando" -> {
                                                        Icons.Rounded.DirectionsWalk
                                                    }
                                                    transporteSeleccionado == "transporte" -> {
                                                        Icons.Rounded.DirectionsBus
                                                    }
                                                    else -> {
                                                        Icons.Rounded.Route
                                                    }
                                                },
                                                contentDescription = ""
                                            )
                                            Spacer(modifier = modifier.padding(horizontal = 3.dp))
                                            if(tiempoDeViajeTemp != null){
                                                if(((tiempoDeViajeTemp/60)/60)!=0){
                                                    Text(
                                                        text = "${((tiempoDeViajeTemp/60)/60)} hrs ",
                                                        style = MaterialTheme.typography.labelSmall
                                                    )
                                                }
                                                Text(
                                                    text = "${tiempoDeViajeTemp%60} min",
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                            Spacer(modifier = modifier.padding(horizontal = 3.dp))
                                            Text(
                                                text = "(${distancia} km)",
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                //Seleccionar transportes-----------------------------------------------------------
                item {
                    Column {

                        Row(
                            modifier = modifier
                                .fillMaxSize()
                                .align(Alignment.CenterHorizontally),
                            horizontalArrangement = Arrangement.SpaceBetween
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
                                selected = transporteSeleccionado == "auto",
                                onClick = {
                                    viewModel.setPolilineaInicializada(false)
                                    transporteSeleccionado = "auto"
                                    calcularZoom(estadoMapa.origen, estadoMapa.destino, viewModel)

                                    apiRutasMapaPrincipal(
                                        destino = estadoMapa.destino,
                                        origen = estadoMapa.origen,
                                        viewModel = viewModel,
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
                                selected = transporteSeleccionado == "transporte",
                                onClick = {
                                    viewModel.setPolilineaInicializada(false)
                                    transporteSeleccionado = "transporte"
                                    calcularZoom(estadoMapa.origen, estadoMapa.destino, viewModel)

                                    apiRutasMapaPrincipal(
                                        destino = estadoMapa.destino,
                                        origen = estadoMapa.origen,
                                        viewModel = viewModel,
                                        travel_mode = "TRANSIT"
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
                                selected = transporteSeleccionado == "caminando",
                                onClick = {
                                    viewModel.setPolilineaInicializadaRuta(false)
                                    transporteSeleccionado = "caminando"
                                    calcularZoom(estadoMapa.origen, estadoMapa.destino, viewModel)

                                    apiRutasMapaPrincipal(
                                        destino = estadoMapa.destino,
                                        origen = estadoMapa.origen,
                                        viewModel = viewModel,
                                        travel_mode = "WALK"
                                    )
                                },
                                colors = colores,
                                label = { Text(text = "caminando") }
                            )
                        }
                    }
                }
            }
        }
    }

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
                Marker(
                    state = rememberMarkerState(position = estadoMapa.destino),
                    onClick = {
                        viewModel.setInformacionInicializada(true)
                        false
                    },
                    draggable = false
                )
            }
            //varios marcadores
            if(estadoMapa.marcadoresInicializado){

                MapEffect(estadoMapa.marcadorInicializado) {
                    val cameraPosition = CameraPosition.fromLatLngZoom(estadoMapa.origen, estadoMapa.zoom)
                    cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
                }

                estadoMapa.marcadores.forEach { marcador ->
                    Marker(
                        state = rememberMarkerState(position = marcador.ubicacion),
                        onClick ={
                            transporteSeleccionado = ""
                            viewModel.setDestino(marcador.ubicacion)
                            viewModel.setInformacionInicializada(false)
                            obtenerMarcadorEntreMuchos(
                                placesClient = placesClient,
                                placeId = marcador.id,
                                viewModel = viewModel
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

                val decodedPolyline: List<LatLng> = PolyUtil.decode(estadoMapa.polilineaCod)

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
                                    transporteSeleccionado = ""
                                    viewModel.setZoom(13f)
                                    viewModel.reiniciarImagenMapaPrincipal()
                                    viewModel.setPolilineaInicializada(false)
                                    viewModel.setMarcadorInicializado(false)
                                    viewModel.setMarcadoresInicializado(false)
                                    viewModel.setInformacionInicializada(false)
                                    CoroutineScope(Dispatchers.Default).launch {

                                        rawJSONUbicacionesCercanas(
                                            filtro = categoria.nombre,
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
                                        transporteSeleccionado = ""
                                        viewModel.setZoom(15f)
                                        viewModel.reiniciarImagenMapaPrincipal()
                                        viewModel.setPolilineaInicializada(false)
                                        viewModel.setMarcadorInicializado(false)
                                        viewModel.setMarcadoresInicializado(false)
                                        viewModel.setInformacionInicializada(false)
                                        obtenerMarcador(
                                            placesClient = placesClient,
                                            placeId = lugar.id,
                                            viewModel = viewModel
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
    DisposableEffect(Unit) {
        onDispose {
            job?.cancel() // Asegura que la corrutina se cancele al salir del composable
        }
    }
}

