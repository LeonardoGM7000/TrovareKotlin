package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Data.Lugar
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv7

@Composable
fun Detalles(
    modifier: Modifier = Modifier,
    placeId: String? = "prueba",
    navController: NavController

){

    //var lugar by remember { mutableStateOf(Lugar("Nombre del lugar", 3.5, "", null, null)) }
    var secondChecked by rememberSaveable { mutableStateOf(false) }

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
                    Row(modifier = modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                    ){
                        FloatingActionButton(
                            modifier = modifier
                                .size(35.dp),
                            onClick = { navController.popBackStack() },
                            containerColor = Color.White,
                            shape = CircleShape
                        ){
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
                        ){
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
                                if(secondChecked){
                                    Icon(
                                        imageVector = Icons.Rounded.Favorite,
                                        contentDescription = ""
                                    )
                                }
                                else {
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
            item {
                Divisor()
            }
            //Nombre del lugar y Rating del lugar---------------------------------------------------
            item {
                Row(
                    modifier = modifier.padding(horizontal = 25.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(0.7f),
                        text = placeId.toString(),
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
                            text = "12/5",
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
                        text = "Direccion del lugar",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            item{
                Row(modifier = modifier
                    .padding(horizontal = 45.dp, vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = modifier.padding(end = 5.dp),
                        imageVector = Icons.Rounded.AccessTime,
                        contentDescription = "",
                        tint = Color.White
                    )
                    Text(
                        text = "Horario de apertura",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
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
                        text = "Pagina web",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }
    }

}


