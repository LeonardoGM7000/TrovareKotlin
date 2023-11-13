package com.example.trovare.ui.theme.Pantallas.Mapa

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.example.trovare.Data.LugarAutocompletar
import com.example.trovare.Data.Places
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv9
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.rememberCameraPositionState
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

    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
    var job: Job? by remember { mutableStateOf(null) }
    val prediccionesBusquedaMapa by remember { mutableStateOf(mutableStateListOf<Places>()) }


    val escom = LatLng(19.504507, -99.147314)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(escom, 15f)
    }


    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {

            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
            }
        //rawJSON(query = textoBuscar.text, recuperarResultados = )
            rawJSON(
                query = textoBuscar.text,
                recuperarResultados = prediccionesBusquedaMapa
            )
        }
    }


    Scaffold(
        topBar = {
            Surface(
                modifier = modifier
                    .wrapContentSize(),
                color = Trv9
            ){
            //Barra de Búsqueda---------------------------------------------------------

            }
        },
    ){
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it),
            contentAlignment = Alignment.TopCenter
        ){
            //Mapa----------------------------------------------------------------------------------
            GoogleMap(
                modifier = modifier
                    .matchParentSize(),
                cameraPositionState = cameraPositionState
            ){

            }
            //Busqueda------------------------------------------------------------------------------
            Column {
                Card(
                    modifier = modifier
                        .padding(horizontal = 25.dp, vertical = 15.dp)
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
                            Icon(
                                imageVector = Icons.Rounded.TravelExplore,
                                contentDescription = ""
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }){
                                Icon(imageVector = Icons.Rounded.FilterList, contentDescription = "")
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
                if (textoBuscar.text == ""){

                } else {
                    Card(
                        modifier = modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.Black),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        LazyColumn(
                            modifier = modifier.padding(5.dp)
                        ){
                            items(prediccionesBusquedaMapa){lugar ->
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
    DisposableEffect(Unit) {
        onDispose {
            job?.cancel() // Asegura que la corrutina se cancele al salir del composable
        }
    }
}





