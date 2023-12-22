package com.example.trovare.ui.theme.Pantallas.Itinerarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trovare.Data.Itinerario
import com.example.trovare.Data.Lugar
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.google.android.gms.maps.model.BitmapDescriptorFactory

@Composable
fun Itinerarios(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel
){

    val usuario by viewModel.usuario.collectAsState()
    var listaVisible by remember { mutableStateOf(true) }
    val lugares = mutableListOf(
        Lugar(id = "ChIJP1dJRVf_0YURr8MZosHl4kI", nombreLugar = "Bosque de Chapultepec", fechaDeVisita = null, horaDeVisita = null, origen = null, ubicacion = null, ruta = null, zoom = 0f, imagen = null ),
        Lugar(id = "ChIJI4_tR83-0YURMouBJIUA4KY", nombreLugar = "Castillo de Chapultepec", fechaDeVisita = null, horaDeVisita = null, origen = null, ubicacion = null, ruta = null, zoom = 0f, imagen = null ),
        Lugar(id = "ChIJwXColAIC0oURBKBWFzsqhkI", nombreLugar = "Zoológico de Chapultepec", fechaDeVisita = null, horaDeVisita = null, origen = null, ubicacion = null, ruta = null, zoom = 0f, imagen = null )
    )
    val itinerarioFalso = Itinerario(nombre = "Chapultepec", autor = "Julio", lugares = lugares, imagen = null, publico = true)

    Scaffold(
        topBar = {
            BarraSuperiorConfig(navController = navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            LazyColumn(){
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Itinerarios",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                item {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            modifier = modifier
                                .padding(start = 25.dp, end = 10.dp),
                            text = "Tus itinerarios",
                            style = MaterialTheme.typography.displaySmall,
                            color = Color.White
                        )
                        FloatingActionButton(
                            modifier = modifier
                                .size(20.dp),
                            onClick = {
                                //Crear nuevo itinerario
                                val nuevoItinerario = Itinerario(
                                    nombre = "nuevo Itinerario",
                                    autor = usuario.nombre,
                                    lugares = null,
                                    imagen = null,
                                    publico = false
                                )
                                navController.navigate(Pantalla.EditarItinerario.ruta)//
                                usuario.itinerarios.add(nuevoItinerario)//
                                viewModel.setItinerarioActual(nuevoItinerario)//
                            },
                            containerColor = Color.White,
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                }
                if(listaVisible){
                    items(usuario.itinerarios){itinerario ->
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp, vertical = 5.dp)
                                .size(100.dp)
                                .clickable {
                                    navController.navigate(Pantalla.EditarItinerario.ruta)
                                    viewModel.setItinerarioActual(itinerario)
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
                                    if (itinerario.imagen != null) {
                                        Image(
                                            bitmap = itinerario.imagen!!,
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
                                    modifier = modifier.fillMaxWidth(0.8f)
                                ) {
                                    Text(
                                        text = itinerario.nombre,
                                        color = Color.Black,
                                        maxLines = 1
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Public,
                                            contentDescription = "",
                                            tint = Color.Black
                                        )
                                        if(itinerario.publico){
                                            Text(
                                                text = "público",
                                                color = Color.Black,
                                                fontSize = 20.sp
                                            )
                                        } else {
                                            Text(
                                                text = "privado",
                                                color = Color.Black,
                                                fontSize = 20.sp
                                            )
                                        }
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = "",
                                            tint = Color.Black
                                        )
                                        Text(
                                            text = itinerario.autor,
                                            color = Color.Black,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                IconButton(
                                    onClick = {
                                        listaVisible = false
                                        viewModel.borrarItinerarioActual(itinerario)
                                        listaVisible = true

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DeleteForever,
                                        contentDescription = "",
                                        tint = Color.Black,
                                    )

                                }
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
                            .padding(start = 25.dp, end = 10.dp),
                        text = "Comunidad",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White
                    )
                }
                if(listaVisible){
                    items(usuario.itinerarios){itinerario ->
                        if(itinerario.publico){
                            Card(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 25.dp, vertical = 5.dp)
                                    .size(100.dp)
                                    .clickable {
                                        navController.navigate(Pantalla.VerItinerario.ruta)
                                        viewModel.setItinerarioActual(itinerario)
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
                                        if (itinerario.imagen != null) {
                                            Image(
                                                bitmap = itinerario.imagen!!,
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
                                        modifier = modifier.fillMaxWidth(0.8f)
                                    ) {
                                        Text(
                                            text = itinerario.nombre,
                                            color = Color.Black,
                                            maxLines = 1
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Rounded.Public,
                                                contentDescription = "",
                                                tint = Color.Black
                                            )
                                            if(itinerario.publico){
                                                Text(
                                                    text = "público",
                                                    color = Color.Black,
                                                    fontSize = 20.sp
                                                )
                                            } else {
                                                Text(
                                                    text = "privado",
                                                    color = Color.Black,
                                                    fontSize = 20.sp
                                                )
                                            }
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Icon(
                                                imageVector = Icons.Rounded.Person,
                                                contentDescription = "",
                                                tint = Color.Black
                                            )
                                            Text(
                                                text = itinerario.autor,
                                                color = Color.Black,
                                                fontSize = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    val itinerario = itinerarioFalso
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                            .size(100.dp)
                            .clickable {
                                navController.navigate(Pantalla.VerItinerario.ruta)
                                viewModel.setItinerarioActual(itinerario)
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
                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.chapultepec),
                                    contentDescription = "",
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                            Column(
                                modifier = modifier.fillMaxWidth(0.8f)
                            ) {
                                Text(
                                    text = itinerario.nombre,
                                    color = Color.Black,
                                    maxLines = 1
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Public,
                                        contentDescription = "",
                                        tint = Color.Black
                                    )
                                    if(itinerario.publico){
                                        Text(
                                            text = "público",
                                            color = Color.Black,
                                            fontSize = 20.sp
                                        )
                                    } else {
                                        Text(
                                            text = "privado",
                                            color = Color.Black,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Icon(
                                        imageVector = Icons.Rounded.Person,
                                        contentDescription = "",
                                        tint = Color.Black
                                    )
                                    Text(
                                        text = itinerario.autor,
                                        color = Color.Black,
                                        fontSize = 20.sp
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


