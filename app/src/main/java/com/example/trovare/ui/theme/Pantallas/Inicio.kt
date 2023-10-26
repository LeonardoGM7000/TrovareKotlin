package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trovare.ui.theme.Recursos.BarraSuperiorInicio
import com.example.trovare.ui.theme.TrovareTheme
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv4
import com.example.trovare.ui.theme.Trv5
import com.example.trovare.ui.theme.transparente


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio() {
    Scaffold(
        topBar = {
            BarraSuperiorInicio()
        },
        bottomBar = {
            MenuInferior()
        }
    ) { it ->
        CuerpoSoporte(padding = it)
    }
}

@Composable
fun CuerpoSoporte(padding: PaddingValues, modifier: Modifier = Modifier){
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        color = Trv1
    ){
        Column {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Categorías",
                style = MaterialTheme.typography.displaySmall
            )
            TarjetaCategorias()
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Explora más sitios",
                style = MaterialTheme.typography.displaySmall
            )
        }

    }
}

@Preview
@Composable
fun preview(){
    TrovareTheme {
        Inicio()
    }
}


@Composable
fun TarjetaCategorias(modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Trv2
        ),
    ){
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Attractions, nombre = "Atracciones")
                Categoria(icono = Icons.Filled.Restaurant, nombre = "Restaurante")
            }
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Museum, nombre = "Museos")
                Categoria(icono = Icons.Filled.Park, nombre = "Parques")
            }
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Hotel, nombre = "Hoteles")
                Categoria(icono = Icons.Filled.Favorite, nombre = "Favoritos")
            }
        }


    }
}
@Composable
fun Categoria(icono: ImageVector, nombre: String,  modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .size(width = 105.dp, height = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Color.White
            )

            {
                Icon(
                    imageVector = icono,
                    contentDescription = ""
                )
            }
            Text(
                text = nombre,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuInferior(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Trv1
    ) {
        Card(
            modifier = modifier.padding(horizontal = 30.dp, vertical = 15.dp),
            colors = CardDefaults.cardColors(Trv4)

        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.Home,
                        contentDescription =  "",
                        tint = Trv5
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription =  "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.Explore,
                        contentDescription =  "",
                        tint = Color.White
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription =  "",
                        tint = Color.White
                    )
                }
            }
        }

    }
}