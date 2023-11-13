package com.example.trovare.ui.theme.Pantallas.Administrador

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarCuentas(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            LazyColumn() {
                item {
                    TituloAdmin(titulo = "ELIMINAR COMENTARIOS")
                }
                item {
                    Divisor( modifier = modifier.padding(15.dp))
                }
                item {
                    BusquedaCuenta(navController = navController)
                }
                item {
                    TarjetaUsuario(usuario = usuarioPrueba)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaCuenta(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }//permite que se abra el teclado automaticamente al abrir la pantalla
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//saber si se esta llevando a cabo una busqueda en el momento(permite mostrar el indicador de progreso circular)
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }

    var job: Job? by remember { mutableStateOf(null) }

    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            busquedaEnProgreso = true//establece que hay una busqueda en progreso (para el indicador de progreso)

            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
            }

            busquedaEnProgreso = false//se acaba el tiempo del timer y se lleva a cabo la busqueda
            //Log.i("test", "terminado")
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(
            modifier = modifier
                .padding(start = 25.dp),
            colors = CardDefaults.cardColors(Color.Black),
            border = CardDefaults.outlinedCardBorder()
        ){
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester),
                value = textoBuscar,
                onValueChange = {
                    textoBuscar = it
                    job?.cancel() // Cancela la corrutina actual si es que existe
                    tiempoRestante = 1//resetea el timer a 1 segundo
                    iniciarTimer()//reinicia la cuenta regresiva del timer
                },
                leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = "") },
                textStyle = MaterialTheme.typography.labelSmall,
                placeholder = { Text(text = "Buscar Cuenta", style = MaterialTheme.typography.labelSmall) },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Black,
                    cursorColor = Color.White,
                )
            )
        }
        Icon(
            modifier = Modifier
                .padding(25.dp)
                .size(28.dp)
                .clickable { mostrarBorrarCuenta = true },
            imageVector = Icons.Default.Delete,
            contentDescription = "",
            tint = Color.Red,
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
            textoConfirmar = "Eliminar Cuentas",
            titulo = "Eliminar Cuentas",
            texto = "¿Estás seguro de eliminar todas las cuentas seleccionadas?",
            icono = Icons.Filled.DeleteForever,
            colorConfirmar = Color.Red
        )
    }
}

@Composable
fun TarjetaUsuario(
    modifier: Modifier = Modifier,
    usuario: Usuario = usuarioPrueba,
){

    var checked = remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Trv1
            ))
        {
            Row() {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Image(
                        painter = painterResource(id = usuario.foto_perfil),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                }
                Text(
                    text = usuario.nombre,
                    modifier = Modifier.padding(18.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Trv3,
                )
            }
        }
        Checkbox(
            modifier = Modifier.padding(end = 13.dp),
            checked = checked.value,
            onCheckedChange = {checked.value = it},
            colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1))
    }
}