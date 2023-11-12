package com.example.trovare.ui.theme.Pantallas.Itinerarios

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1

@Composable
fun Itinerarios(
    modifier: Modifier = Modifier,
    navController: NavController
){
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
                        onClick = { navController.navigate(Pantalla.EditarItinerario.ruta) },
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