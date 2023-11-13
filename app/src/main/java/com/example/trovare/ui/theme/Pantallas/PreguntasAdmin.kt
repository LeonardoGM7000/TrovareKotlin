package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv4
import com.example.trovare.ui.theme.Trv5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreguntasAdmin(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ){
                FloatingActionButton(
                    onClick = { /*TODO*/ }, //falta agregar función de agregar pregunta
                    containerColor = Trv4,
                    modifier = Modifier
                        .padding(25.dp)
                        .size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        tint = Trv5,
                        modifier = Modifier
                            .size(40.dp)
                    )
                }
            }
        }
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            LazyColumn() {
                item {
                    TituloAdmin(titulo = "EDITAR PREGUNTAS")
                }
                item {
                    Divisor(modifier = modifier.padding(15.dp))
                }
                item{
                    TarjetaPreguntas(pregunta = "¿Cómo informar errores a soporte técnico?",
                        navController = navController)
                }
                item{
                    TarjetaPreguntas(modifier = modifier.padding(top = 20.dp),
                        pregunta = "¿Cómo iniciar sesión?",
                        navController = navController)
                }
                item{
                    TarjetaPreguntas(modifier = modifier.padding(top = 20.dp),
                        pregunta = "¿Cómo puedo obtener información para saber llegar a un lugar?",
                        navController = navController)
                }
                item{
                    TarjetaPreguntas(modifier = modifier.padding(top = 20.dp),
                        pregunta = "¿Cómo puedo agregar una localización?",
                        navController = navController)
                }
            }
        }
    }
}

@Composable
fun TarjetaPreguntas(
    modifier: Modifier = Modifier,
    pregunta: String = "",
    navController: NavController
){

    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        colors = CardDefaults
            .cardColors(containerColor = Trv1),
    ) {
        Column {
            Row(modifier = Modifier
                .fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier
                        .padding(13.dp),
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    tint = Color.White,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 8.dp),
                        text = pregunta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Icon(
                    modifier = Modifier
                        .padding(13.dp)
                        .clickable { navController.navigate(Pantalla.EditarPreguntas.ruta) }, //necesita abrir la info de su pregunta
                    imageVector = Icons.Rounded.Edit,
                    contentDescription = "",
                    tint = Color.White
                )
                Icon(
                    modifier = Modifier
                        .padding(13.dp)
                        .clickable { mostrarBorrarCuenta = true },
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = Color.White,
                )
                VentanaDeAlerta(
                    mostrar = mostrarBorrarCuenta,
                    alRechazar = {mostrarBorrarCuenta = false},
                    alConfirmar = { //Necesita eliminar la pregunta
                        navController.navigate(Pantalla.Administrador.ruta){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                    },
                    textoConfirmar = "Borrar Pregunta",
                    titulo = "Borrar Pregunta",
                    texto = "¿Quieres borrar la pregunta frecuente?",
                    icono = Icons.Filled.DeleteForever,
                    colorConfirmar = Color.Red
                )
            }
        }

    }
}