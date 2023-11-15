package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv7
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun Detalles(
    modifier: Modifier = Modifier,
    placeId: String? = "",
    placesClient: PlacesClient,
    viewModel: TrovareViewModel,
    navController: NavController
){

    //var lugar by remember { mutableStateOf(Lugar("Nombre del lugar", 3.5, "", null, null)) }
    var secondChecked by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var numeroTelefono by rememberSaveable { mutableStateOf("") }
    var paginaWeb by rememberSaveable { mutableStateOf("") }
    var calificacion by rememberSaveable { mutableStateOf(-1.0) }
    var latLng by rememberSaveable { mutableStateOf(LatLng(0.0,0.0)) }
    //var imagen by rememberSaveable { mutableStateOf(ImageBitmap(500,500)) }


    LaunchedEffect(key1 = Unit){
        viewModel.obtenerLugar(
            placesClient = placesClient,
            placeId = placeId?: "",
            nombre = { nombre = it?:"" },
            direccion = { direccion = it?:""},
            rating =  { calificacion = it?: -1.0},
            numeroTelefono = {numeroTelefono = it?: ""},
            paginaWeb = {paginaWeb = it?: ""},
            latLng = {latLng = it?: LatLng(0.0,0.0) },
            //imagen = {imagen = it}
        )
    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv7
    ) {
        LazyColumn(){
            //Tarjeta que muestra la imagen, botón de regreso y botón de agregar a favoritos--------
            item {
                Card(
                    modifier = modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .padding(start = 45.dp, top = 25.dp, end = 45.dp)
                        .aspectRatio(1F),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
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

                        Row(
                            modifier = modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        ) {
                            FloatingActionButton(
                                modifier = modifier
                                    .size(35.dp),
                                onClick = { navController.popBackStack() },
                                containerColor = Color.White,
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = modifier.fillMaxWidth(0.86f))
                            FloatingActionButton(
                                modifier = modifier
                                    .size(35.dp),
                                onClick = { /*TODO*/ },
                                containerColor = Color.White,
                                shape = CircleShape
                            ) {
                                IconToggleButton(
                                    checked = secondChecked,
                                    onCheckedChange = { checked -> secondChecked = checked },
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        containerColor = Color.White,
                                        checkedContainerColor = Color.White,
                                        contentColor = Color.Black,
                                        checkedContentColor = Color.Red
                                    )
                                ) {
                                    if (secondChecked) {
                                        Icon(
                                            imageVector = Icons.Rounded.Favorite,
                                            contentDescription = ""
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.FavoriteBorder,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Divisor()
            }
            //Nombre del lugar y Rating del lugar---------------------------------------------------
            if(nombre != ""){
                item {
                    Row(
                        modifier = modifier.padding(horizontal = 25.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(0.7f),
                            text = nombre,
                            textAlign = TextAlign.Justify,
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall
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
                }
            }
            //Direccion-----------------------------------------------------------------------------
            if(direccion != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
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
            }
            //Horario de apertura-------------------------------------------------------------------
            if(numeroTelefono != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
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
            }
            //Pagina web----------------------------------------------------------------------------
            if(paginaWeb != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
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
            //Mapa con la ubicaci[on del lugar------------------------------------------------------
            item {
                Box(
                    Modifier
                        //.padding(horizontal = 25.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    }
                    val estadoMarcador = rememberMarkerState(position = latLng)

                    if(latLng != LatLng(0.0,0.0)){
                        GoogleMap(
                            modifier = modifier
                                .matchParentSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(mapType = MapType.NORMAL, isBuildingEnabled = false),
                            uiSettings = MapUiSettings(
                                scrollGesturesEnabled = false,
                                scrollGesturesEnabledDuringRotateOrZoom = false,
                                zoomGesturesEnabled = false,
                                zoomControlsEnabled = false
                            )
                        ){
                            Marker(
                                state = estadoMarcador,
                                title = nombre,
                                //onClick = markerClick
                            )
                        }
                    }

                }
            }
            item {
                Divisor()
            }
            item {
                Text(
                    modifier = modifier
                        .padding(horizontal = 25.dp),
                    text = "Reseñas",
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            item {

                /*
                Image(
                    bitmap = imagen,
                    contentDescription = null, // Puedes proporcionar una descripción si es necesario
                    modifier = Modifier.fillMaxSize() // Ajusta el tamaño de la imagen según sea necesario
                )*/

            }
        }
    }
}




