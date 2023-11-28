package com.example.trovare.ui.theme.Pantallas.Itinerarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trovare.Data.Itinerario
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3

@Composable
fun Itinerarios(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel
){

    val usuario by viewModel.usuario.collectAsState()

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv1
    ) {
        LazyColumn(){
            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
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
                            navController.navigate(Pantalla.EditarItinerario.ruta)
                            usuario.itinerarios.add(Itinerario(nombre = "nuevo Itinerario", actividades = null))
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
            items(usuario.itinerarios){itinerario ->
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 25.dp, vertical = 5.dp)
                        .size(100.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Trv3
                    )
                ) {
                    Row {
                        Card(
                            modifier = modifier
                                .padding(5.dp)
                                .aspectRatio(1f),
                        ) {
                            Image(
                                modifier = modifier
                                    .fillMaxSize(),
                                painter = painterResource(id = R.drawable.image_placeholder),
                                contentDescription = ""
                            )
                        }
                        Column {
                            Text(
                                text = itinerario.nombre,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Public,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "lugar",
                                    color = Color.Black,
                                    fontSize = 20.sp
                                )
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
                                    text = "lugar",
                                    color = Color.Black,
                                    fontSize = 20.sp
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
        }
    }
}