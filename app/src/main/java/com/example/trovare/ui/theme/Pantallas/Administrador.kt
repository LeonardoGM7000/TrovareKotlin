package com.example.trovare.ui.theme.Pantallas


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1

<<<<<<< HEAD
=======
var Buscar:String = ""

>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
@Composable
fun Administrador(
    modifier: Modifier = Modifier,
    navController: NavController
){

    var mostrarCerrarSesion by rememberSaveable { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv1
    ){
        LazyColumn(){
            item{
                TituloAdmin(titulo = "ADMINISTRADOR")
            }
            item {
                Divisor( modifier = modifier.padding(15.dp))
            }
            item{
                TarjetaNormal( //función está en Pantalla de Config
                    titulo = "Editar Preguntas Frecuentes",
                    icono = Icons.Filled.Info,
                    accion = {navController.navigate(Pantalla.PreguntasAdmin.ruta)}
                )
                TarjetaNormal(
                    modifier = modifier.padding(top = 5.dp),
                    titulo = "Eliminar Cuentas",
                    icono = Icons.Filled.DeleteForever,
                    accion = {navController.navigate(Pantalla.EliminarCuentas.ruta)}
                )
                TarjetaNormal(
                    modifier = modifier.padding(top = 5.dp),
                    titulo = "Eliminar Comentarios",
                    icono = Icons.Filled.Comment,
                    accion = {navController.navigate(Pantalla.EliminarComentarios.ruta)}
                )
                TarjetaNormal(
                    modifier = modifier.padding(top = 5.dp),
                    titulo = "Cerrar Sesión",
                    icono = Icons.Filled.Info,
                    accion = {mostrarCerrarSesion = true}
                )
                VentanaDeAlerta( //función está en Recursos.kt
                    mostrar = mostrarCerrarSesion,
                    alRechazar = {mostrarCerrarSesion = false},
                    alConfirmar = {
                        navController.navigate(Pantalla.Bienvenida.ruta){
                            popUpTo(navController.graph.id){
                                inclusive = true
                            }
                        }
                    },
                    textoConfirmar = "Cerrar Sesión",
                    titulo = "Cerrar Sesión",
                    texto = "¿Quiéres cerrar sesión en Trovare?",
                    icono = Icons.Filled.Logout
                )
            }
        }

    }
}

@Composable
fun TituloAdmin(
    modifier: Modifier = Modifier,
    titulo: String = "",
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Icon(
            modifier = modifier
                .size(45.dp)
                .padding(top = 8.dp),
            imageVector = Icons.Default.Construction,
            contentDescription = "",
            tint = Color.White
        )
        Text(
            modifier = modifier.padding(start = 5.dp),
            text = titulo,
            color = Color.White,
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.displayMedium
        )
    }
}