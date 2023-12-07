package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Api.rawJSONLugarCercano
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarruselSeleccionado(
    categoria: String,
    placesClient: PlacesClient,
    viewModel: TrovareViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val lista = listOf(
        "ChIJt0aQMHL50YURmSGHZtqqYIs",
        "ChIJFzoDI5X50YURKYeRnBn0AtU"
        )

    val lugaresCercanos by remember { mutableStateOf(mutableStateListOf<NearbyPlaces>()) }
    val lugaresId by remember { mutableStateOf(mutableStateListOf<String>()) }
    val ubicacion by viewModel.ubicacion.collectAsState()
    var calificacion by rememberSaveable { mutableStateOf(-1.0) }

    //Rutina que se ejecuta al componer la página, recupera los lugares cercanos dependiendo del filtro
    LaunchedEffect(key1 = Unit){
        CoroutineScope(Dispatchers.Default).launch {
            //Funcion de API que permite recuperar lugares cercanos con base en un filtro-----------
            CoroutineScope(Dispatchers.Default).launch {
                delay(200)
                rawJSONLugarCercano(
                    filtro = categoria,
                    recuperarResultados = lugaresCercanos,
                    recuperarId = lugaresId,
                    ubicacion = ubicacion
                )
            }
            viewModel.obtenerFotoLugarCercano(placesClient = placesClient, placesId = lista)
        }
    }
    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ){
            LazyColumn{
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = categoria,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                items(lugaresCercanos){lugar ->
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                            .size(100.dp)
                            .clickable {
                                navController.navigate(Pantalla.Detalles.conArgs(lugar.id))
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Trv3
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Card(
                                modifier = modifier
                                    .padding(5.dp)
                                    .aspectRatio(1f),
                            ) {
                                var imagen = viewModel.imagen.value

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
                            Column {
                                Text(
                                    text = lugar.displayName?.text ?: "",
                                    color = Color.Black,
                                    maxLines = 1
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        modifier = modifier
                                            .padding(end = 5.dp, bottom = 5.dp),

                                        onClick = {
                                            navController.navigate(Pantalla.Detalles.conArgs(lugar.id))
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White //Hay que cambiarle el color a Trv12
                                        )
                                    ) {
                                        Text(
                                            text = "Ver Detalles",
                                            color = Color.Black
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(5.dp, 5.dp)) {
                                        Text(
                                            text = "${calificacion}/5",
                                            style = MaterialTheme.typography.labelSmall
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
                    }
                }
            }
        }
    }
}

/*
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(Pantalla.Detalles.conArgs(lugar.id))
                            },
                        colors = CardDefaults.cardColors(Trv1),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Column(
                            modifier = modifier.padding(top = 5.dp, end = 5.dp, bottom = 5.dp)
                        ) {
                            Text(
                                modifier = modifier.padding(3.dp),
                                text = lugar.displayName?.text ?: "",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                modifier = modifier.padding(3.dp),
                                text = lugar.shortFormattedAddress?: "",
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Justify,
                                color = Color.White
                            )
                        }
                    }
 */

