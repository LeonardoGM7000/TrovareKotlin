package com.example.trovare.ui.theme.Pantallas

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Data.LugarAutocompletar
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Buscar(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel,
    placesClient: PlacesClient
){
    val focusRequester = remember { FocusRequester() }//permite que se abra el teclado automaticamente al abrir la pantalla
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//saber si se esta llevando a cabo una busqueda en el momento(permite mostrar el indicador de progreso circular)
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }//tiempo antes de que se haga la llamada a la API de places(1 segundo)
    var job: Job? by remember { mutableStateOf(null) }

    val prediccionesAutocompletar by remember { mutableStateOf(mutableStateListOf<LugarAutocompletar>()) }//lista de lugares entregados por la API para autocompletar

    //Al escribir en la barra de busqueda se activa un timer de un segundo el cual se reinicia cada que se modifica el texto de la barra de busqueda
    //con la finalidad de que no se haga una llamada a la API en cada modificaci[on del texto


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


    
    LaunchedEffect(key1 = Unit){
        focusRequester.requestFocus()//manda el focus request al textField al iniciar por primera vez la pantalla
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = modifier
                    .wrapContentSize(),
                color = Trv1
            ){
                Column {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = modifier.padding(start = 15.dp, top = 15.dp),
                            onClick = {
                                navController.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                        }
                        //Barra de Búsqueda---------------------------------------------------------
                        Card(
                            modifier = modifier
                                .padding(start = 25.dp, end = 25.dp, top = 15.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Color.Black),
                            border = CardDefaults.outlinedCardBorder()
                        ){
                            TextField(
                                modifier = modifier
                                    .focusRequester(focusRequester)
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
                    Divisor()
                }
            }

        },
        //Contenido de la busqueda *(indicador de progreso circular para busqueda o busquedas encontradas)
    ){

        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ){
            if (busquedaEnProgreso){
                Box(modifier = modifier.fillMaxSize()){
                    CircularProgressIndicator(
                        modifier = modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

            } else {
                LazyColumn {
                    items(prediccionesAutocompletar) { lugar ->
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
                                modifier = modifier.padding(5.dp)
                            ) {
                                Text(
                                    text = lugar.textoPrimario,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = lugar.textoSecundario,
                                    style = MaterialTheme.typography.bodySmall
                                )
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





