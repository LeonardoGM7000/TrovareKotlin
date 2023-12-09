package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.google.android.libraries.places.api.net.PlacesClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaSeleccionada(
    categoria: String,
    placesClient: PlacesClient,
    viewModel: TrovareViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
){

    val estadoMapa by viewModel.estadoMapa.collectAsState()
    val lugaresCercanos by remember { mutableStateOf(mutableStateListOf<NearbyPlaces>()) }
    val lugaresId by remember { mutableStateOf(mutableStateListOf<String>()) }

    //Rutina que se ejecuta al componer la página, recupera los lugares cercanos dependiendo del filtro
    /*
    LaunchedEffect(key1 = Unit){
        //Funcion de API que permite recuperar lugares cercanos con base en un filtro-----------
        CoroutineScope(Dispatchers.Default).launch {
            delay(200)

            rawJSONLugarCercano(
                filtro = categoria,
                recuperarResultados = lugaresCercanos,
                recuperarId = lugaresId,
                ubicacion = estadoMapa.destino
            )
        }
    }

     */
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
            LazyColumn(){
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
                                text = /*lugar.shortFormattedAddress?:*/  "",

                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Justify,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}