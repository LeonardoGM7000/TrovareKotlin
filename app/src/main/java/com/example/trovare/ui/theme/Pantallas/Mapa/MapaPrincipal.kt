package com.example.trovare.ui.theme.Pantallas.Mapa

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.FilterListOff
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Api.rawJSON
import com.example.trovare.Data.Places
import com.example.trovare.Data.categorias
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv9
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaPrincipal(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel,
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

            //rawJSON(query = textoBuscar.text, recuperarResultados = )
            rawJSON(
                query = textoBuscar.text,
                recuperarResultados = prediccionesBusquedaMapa
            )
        }
    }

    //Mapa-------------------------------
    val visible by viewModel.visible.collectAsState()
    val nombreLugar by remember { mutableStateOf(null) }

    //Filtros----------------------------
    var filtroExtendido by rememberSaveable { mutableStateOf(false) }


    //UI--------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------

    Box(modifier = modifier
        .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        //Mapa--------------------------------------------------------------------------------------
        if(visible){

            //variables------------------
            val ubicacion by viewModel.ubicacion.collectAsState()
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(ubicacion, 15f)
            }
            val estadoMarcador = rememberMarkerState(key = "1", position = ubicacion)


            GoogleMap(
                modifier = modifier
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState,
                //locationSource = LocationSource
            ){
                Marker(
                    state = estadoMarcador,
                    title = nombreLugar,
                    //onClick =
                )

            }
        }
        else{
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Surface(
                    modifier = modifier.fillMaxSize(),
                    color = Trv9
                ) {
                    Box(modifier = modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }
                }

            }


        }

        Column {
            //Busqueda------------------------------------------------------------------------------
            Card(
                modifier = modifier
                    .padding(start = 25.dp, top = 15.dp, end = 25.dp, bottom = 5.dp)
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
                    placeholder = { Text(text = "Busca lugares de interés", style = MaterialTheme.typography.labelSmall) },
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        containerColor = Color.Black,
                        cursorColor = Color.White,
                    )
                )
            }
            //Mostrar filtros-----------------------------------------------------------------------
            if(filtroExtendido){
                LazyRow(
                    modifier = modifier
                        .padding(horizontal = 25.dp)
                ){
                    items(categorias){categoria ->
                        Card(
                            modifier = modifier.padding(end = 5.dp),
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
                                        viewModel.obtenerMarcador(
                                            placesClient = placesClient,
                                            placeId = lugar.id,
                                        )
                                        viewModel.setVisible(false)
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