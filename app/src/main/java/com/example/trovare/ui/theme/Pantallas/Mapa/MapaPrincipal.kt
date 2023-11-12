package com.example.trovare.ui.theme.Pantallas.Mapa

import androidx.compose.foundation.layout.Box
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Data.LugarAutocompletar
import com.example.trovare.ui.theme.ViewModel.TrovareViewModel
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
    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//saber si se esta llevando a cabo una busqueda en el momento(permite mostrar el indicador de progreso circular)
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
    var job: Job? by remember { mutableStateOf(null) }

    val escom = LatLng(19.504507, -99.147314)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(escom, 15f)
    }

    val prediccionesAutocompletar by remember { mutableStateOf(mutableStateListOf<LugarAutocompletar>()) }

    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            busquedaEnProgreso = true//establece que hay una busqueda en progreso (para el indicador de progreso)

            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
            }

            busquedaEnProgreso = false//se acaba el tiempo del timer y se lleva a cabo la busqueda
            //Log.i("test", "terminado")
            viewModel.autocompletar(placesClient = placesClient, query = textoBuscar.text, listaLugares = prediccionesAutocompletar)
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
                        leadingIcon = {Icon(imageVector = Icons.Rounded.TravelExplore, contentDescription = "")},
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
            }
        },
    ){
        Box(modifier = modifier
            .fillMaxSize()
            .padding(it),
            contentAlignment = Alignment.BottomCenter
        ){
            GoogleMap(
                modifier = modifier
                    .matchParentSize(),
                cameraPositionState = cameraPositionState
            ){

            }
        }
    }
}





