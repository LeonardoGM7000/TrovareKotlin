package com.example.trovare.ui.theme.Pantallas

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.R
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Perfil.fotoDePerfilUsuarioo
import com.example.trovare.ui.theme.Pantallas.Perfil.reseña
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

data class reseñaAdmin(
    val Nombre:String, val fecha: String, val foto:String, val placeId: String,
    var descripcion: String, var calificacion: String, val revisar: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarComentarios(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val db = Firebase.firestore
    var reseñasPropias by remember { mutableStateOf(mutableListOf<reseñaAdmin>()) }

        val reseñasTrovare  = db.collection("Reseña").document("datos")
        reseñasTrovare.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val datosActuales = documentSnapshot.data

                datosActuales?.let {
                    val campoDataReseñas = it["dataReseñas"] as? MutableList<HashMap<String, Any>>

                    campoDataReseñas?.forEach { reseñaMap ->
                        val usuarioActual = reseñaMap["nombre"].toString()
                        val estrellas = reseñaMap["calificacion"].toString()
                        val textoComentario = reseñaMap["descripcion"].toString()
                        val fecha = reseñaMap["fecha"].toString()
                        val foto = reseñaMap["foto"].toString()
                        val placeId  = reseñaMap["placeId"]
                        val reportado  = reseñaMap["revisar"].toString()

                        Log.i("booleano",reportado)

                        // Crear una instancia de Resena con los datos obtenidos de Firestore
                        val nuevaReseña = reseñaAdmin(usuarioActual,fecha,foto,placeId.toString(),textoComentario, estrellas,reportado)
                        // Agregar la nueva reseña a la lista mutable reseñasPropias
                        reseñasPropias.add(nuevaReseña)
                        Log.i("tamaño",reseñasPropias.size.toString())
                    }
                }
            } else {
                println("El documento 'datos' en la colección 'Reseña' no existe en Firestore")
            }
        }.addOnFailureListener { e ->
            println("Error al obtener el documento 'datos' en Firestore: $e")
        }
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
                    TarjetaBusqueda(navController = navController)
                }
                item{

                    Log.i("Resenaspropiasss",reseñasPropias.size.toString())
                    val reseñasFiltradas = reseñasPropias.filter { it.revisar == "true" }

// Mostrar las reseñas filtradas utilizando TarjetaReseña
                    if (reseñasFiltradas.isNotEmpty()) {
                        reseñasFiltradas.forEach { reseña ->
                            // Crear la tarjeta de reseña para cada elemento de reseñasFiltradas
                            //val aux = reseñaAdmin(reseña.Nombre,reseña.calificacion.toInt(),reseña.descripcion,reseña.fecha.toInt(),reseña.foto,reseña.revisar)
                            TarjetaReseñaAdmin(reseña = reseña, onDeleteClick = {}, clave = "", APIoApp = false, navController = navController)
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                    } else {
                        // No hay reseñas que coincidan con el placeId dado
                        Text(
                            text = "No hay reseñas para este lugar.",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarjetaBusqueda(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var mostrarBorrarComentario by rememberSaveable { mutableStateOf(false) }

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
                leadingIcon = {Icon(imageVector = Icons.Rounded.Search, contentDescription = "")},
                textStyle = MaterialTheme.typography.labelSmall,
                placeholder = { Text(text = "Buscar Comentario", style = MaterialTheme.typography.labelSmall) },
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
                .clickable { mostrarBorrarComentario = true },
            imageVector = Icons.Default.Delete,
            contentDescription = "",
            tint = Color.Red,
        )
        VentanaDeAlerta(
            mostrar = mostrarBorrarComentario,
            alRechazar = {mostrarBorrarComentario = false},
            alConfirmar = { //Necesita eliminar la pregunta
                navController.navigate(Pantalla.Administrador.ruta){
                    popUpTo(navController.graph.id){
                        inclusive = true
                    }
                }
            },
            textoConfirmar = "Eliminar Comentarios",
            titulo = "Eliminar Comentarios",
            texto = "¿Estás seguro de eliminar todos los comentarios seleccionados?",
            icono = Icons.Filled.DeleteForever,
            colorConfirmar = Color.Red
        )
    }
}

@Composable
fun TarjetaComentarios(
    modifier: Modifier = Modifier,
    comentario: String = "",
    usuario: Usuario = usuarioPrueba, //filtrar por comentarios reportados
){
    var checked = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier
            .padding(horizontal = 15.dp, vertical = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Trv3
            ),
        ) {
            Row() {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = usuario.foto_perfil),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                }
                Column(
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = usuario.nombre,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                    Text(
                        modifier = Modifier.padding(end = 10.dp, bottom = 10.dp),
                        text = comentario,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                    )
                }

            }
        }
        Checkbox(
            checked = checked.value,
            onCheckedChange = {checked.value = it},
            colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1))
    }
}

@Composable
fun TarjetaReseñaAdmin(reseña: reseñaAdmin, modifier: Modifier = Modifier,
                         clave: String, APIoApp: Boolean, onDeleteClick: () -> Unit,
                         navController: NavController
) {
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var checked = remember { mutableStateOf(false) }

    val cardSizeModifier = Modifier
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        )

    val tiempoActual = System.currentTimeMillis() // Tiempo actual en milisegundos
    val tiempoResena = TimeUnit.SECONDS.toMillis(reseña.fecha.toLong()) // Convertir a milisegundos

    val tiempoTranscurrido = DateUtils.getRelativeTimeSpanString(tiempoResena, tiempoActual, DateUtils.MINUTE_IN_MILLIS)
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable {}
                .then(cardSizeModifier) // Aplicar el modificador de tamaño aquí
                .padding(horizontal = 25.dp),
        ) {
            Column(
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    fotoDePerfilAdmin(url = reseña.foto)

                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth(0.64f)
                    ) {
                        Text(
                            text = reseña.Nombre,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = tiempoTranscurrido.toString(), // Mostrar tiempo transcurrido
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                    Checkbox(
                        modifier = Modifier.padding(end = 13.dp),
                        checked = checked.value,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                // Agregar la cuenta a la lista de cuentas seleccionadas si el Checkbox está marcado
                                //cuentasSeleccionadas.add(cuenta)
                                Log.i("SE AÑADIO CORRECTAMENTE: cuenta.nombre","");
                            } else {
                                // Quitar la cuenta de la lista de cuentas seleccionadas si el Checkbox se desmarca
                                // cuentasSeleccionadas.remove(cuenta)
                                Log.i("SE QUITO DE LA LISTA: cuenta.nombre","");
                            }
                            // Actualizar el valor de checked.value

                            checked.value = isChecked
                        },
                        colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column (
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                    ) {
                        Text(
                            modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                            text = reseña.descripcion,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun fotoDePerfilAdmin(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(80.dp, 80.dp)
            .clip(CircleShape)
            .padding(13.dp)
    )
}