package com.example.trovare.ui.theme.Pantallas

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Data.Configuracion
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.listaDeConfiguracion
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.TrovareTheme
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configuracion(
    modifier: Modifier = Modifier,
    viewModel: TrovareViewModel,
    navController: NavController
) {

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
    ) { it ->

        var mostrarCerrarSesion by rememberSaveable { mutableStateOf(false) }
        var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
        val uiState by viewModel.uiState.collectAsState()

        //CUERPO DE LA PANTALLA CONFIGURACION ------------------------------------------------------
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
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Configuración",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item{
                    TarjetaPerfil(
                        navController = navController
                    )
                }
                item {
                    Divisor()
                }
                item {
                    //Configuración-Idioma----------------------------------------------------------
                    TarjetaConfiguracion(
                        configuracion = listaDeConfiguracion[0],
                        configuracionActual = uiState.idioma
                    ) {
                        viewModel.setIdioma(it)
                    }
                    //Configuración-Unidad----------------------------------------------------------
                    TarjetaConfiguracion(
                        configuracion = listaDeConfiguracion[1],
                        configuracionActual = uiState.unidad
                    ) {
                        viewModel.setUnidades(it)
                    }
                    //Configuración-Moneda----------------------------------------------------------
                    TarjetaConfiguracion(
                        configuracion = listaDeConfiguracion[2],
                        configuracionActual = uiState.moneda
                    ) {
                        viewModel.setMonedas(it)
                    }
                }
                item {
                    TarjetaNormal(
                        titulo = "Soporte",
                        icono = Icons.Filled.Help,
                        accion = {navController.navigate(Pantalla.Soporte.ruta)}
                    )
                    TarjetaNormal(
                        titulo = "FAQs",
                        icono = Icons.Filled.Info,
                        accion = {navController.navigate(Pantalla.FAQS.ruta)}
                    )
                    Divisor()
                    TarjetaNormal(
                        titulo = "Cerrar Sesión",
                        icono = Icons.Filled.Logout,
                        accion = { mostrarCerrarSesion = true }
                    )
                    TarjetaNormal(
                        titulo = "Borrar cuenta",
                        icono = Icons.Filled.DeleteForever,
                        color = Color.Red,
                        accion = { mostrarBorrarCuenta = true}
                    )
                    VentanaDeAlerta(
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
                    VentanaDeAlerta(
                        mostrar = mostrarBorrarCuenta,
                        alRechazar = {mostrarBorrarCuenta = false},
                        alConfirmar = {
                            navController.navigate(Pantalla.Bienvenida.ruta){
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        },
                        textoConfirmar = "Borrar Cuenta",
                        titulo = "Borrar Cuenta",
                        texto = "¿Quiéres borrar tu cuenta de Trovare? No se guardarán tus datos y tendrás que crear una nueva si es que deseas usar la aplicación",
                        icono = Icons.Filled.DeleteForever,
                        colorConfirmar = Color.Red
                    )

                }
            }
        }

    }
}

@Composable
fun TarjetaPerfil(
        modifier: Modifier = Modifier,
        viewModel: PerfilDataModel = viewModel(),
        navController: NavController,
    ){

    val usuario by viewModel.dato
    LaunchedEffect(true){
        viewModel.obtenerDato()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Trv2
        ),
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
        ){
            Card(
                modifier = modifier
                    .padding(10.dp)
                    .size(80.dp),
                shape = RoundedCornerShape(100.dp)
            ) {
                Image(
                    modifier = modifier.fillMaxSize(),
                    painter = painterResource(id = usuario.foto_perfil),
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = modifier
                        .padding(vertical =  5.dp),
                    text = usuario.nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,

                )
                Text(
                    modifier = modifier
                        .clickable { navController.navigate(Pantalla.PerfilConfiguracion.ruta) },
                    text = "Ver Perfil",
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun TarjetaConfiguracion(
    modifier: Modifier = Modifier,
    configuracion: Configuracion,
    configuracionActual: String,
    accion: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedValue by rememberSaveable { mutableStateOf(configuracion.opciones.first()) }


    // Definir la animación de tamaño de la tarjeta
    val cardSizeModifier = Modifier
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource()
            ) { expanded = !expanded }
            .then(cardSizeModifier),  // Aplicar el modificador de tamaño aquí
        colors = CardDefaults.cardColors(
            containerColor = Trv1
        ),
    ) {
        Column() {
            Row(modifier = modifier
                .fillMaxSize()
            ) {
                Icon(
                    modifier = modifier
                        .padding(13.dp),
                    imageVector = configuracion.icono,
                    contentDescription = "",
                    tint = Color.White,
                )
                Box(
                    modifier = modifier
                        .fillMaxWidth(0.5F)
                ){
                    Text(
                        modifier = modifier
                            .padding(top = 8.dp)
                            .fillMaxHeight(),
                        text = configuracion.nombreDeConfig,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = modifier
                        .fillMaxSize(0.65F)

                ){
                    Text(
                        modifier = modifier
                            .padding(top = 10.dp)
                            .fillMaxSize(),
                        text = configuracionActual,
                        textAlign = TextAlign.Right,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                BotonPregunta(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            if (expanded) {
                //configuracion.contenido()
                //OPCIONES DE CONFIGURACION---------------------------------------------------------

                Column(
                    modifier = modifier.padding(start = 35.dp)
                ){
                    configuracion.opciones.forEach { item ->
                        Row(
                            modifier = Modifier
                                .selectable(
                                    selected = configuracionActual == item,
                                    onClick = {
                                        selectedValue = item
                                        accion(item)
                                    }
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            RadioButton(
                                selected = configuracionActual == item,
                                onClick = {
                                    selectedValue = item
                                    accion(item)
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = Trv6)
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
    Divisor()
}

//Configuracion------------------------------------------------

@Composable
fun TarjetaNormal(
    modifier: Modifier = Modifier,
    titulo: String = "",
    icono: ImageVector,
    accion: () -> Unit = {},
    color: Color = Color.White,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp)
            .clickable(onClick = accion),
        colors = CardDefaults
            .cardColors(containerColor = Trv1),
    ) {
        Column() {
            Row(modifier = modifier
                .fillMaxSize()
            ) {
                Icon(
                    modifier = modifier
                        .padding(13.dp),
                    imageVector = icono,
                    contentDescription = "",
                    tint = color,
                )
                Box(
                    modifier = modifier
                        .fillMaxWidth(0.82F)
                ){
                    Text(
                        modifier = modifier
                            .fillMaxHeight()
                            .padding(top = 8.dp),
                        text = titulo,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
                Icon(
                    modifier = modifier
                        .padding(13.dp),
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = color,
                )
            }
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewConfiguracion() {
    TrovareTheme {

    }
}

 */
