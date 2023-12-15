package com.example.trovare.ui.theme.Pantallas.Perfil

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.trovare.Data.usuarioPrueba
//import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Cuenta
import com.example.trovare.ui.theme.Pantallas.Question
import com.example.trovare.ui.theme.Pantallas.Resena
import com.example.trovare.ui.theme.Pantallas.TarjetaReseña
import com.example.trovare.ui.theme.Pantallas.effectKey
import com.example.trovare.ui.theme.Pantallas.fotoDePerfilUsuario
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

data class reseña(val Nombre:String, val fecha: String, val foto:String, val placeId: String,
                          var descripcion: String, var calificacion: String, val revisar: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilConfiguracion(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel
) {
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
            PerfilPrincipal(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilInicio(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel
) {
    Scaffold(
        topBar = {
            BarraSuperiorConfig(navController = navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            PerfilPrincipal(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilPrincipal(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel,
){

    val usuario by viewModel.usuario.collectAsState()
    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    var usuarioReseñas by remember { mutableStateOf(emptyList<reseña>())}
    var questions by remember { mutableStateOf(emptyList<Question>()) }
    var isLoading by remember { mutableStateOf(true) }
    val auth: FirebaseAuth by lazy { com.google.firebase.Firebase.auth }

    //obtener datos de reseñas de usuario para mostrarlas en el perfil
    //LaunchedEffect(effectKey) {
        firestore.collection("Usuario").document(auth.currentUser?.email.toString()).collection("comentarios").get()
            .addOnSuccessListener { commentsCollection ->
                var listaReseñas = mutableListOf<reseña>()
                for (reseñaDocument in commentsCollection) {
                    val reseñaData = reseña(Nombre = reseñaDocument.getString("nombre") ?: "",
                        fecha = reseñaDocument.getString("fecha") ?: "",
                        foto = reseñaDocument.getString("foto") ?: "",
                        placeId = reseñaDocument.getString("placeId") ?: "",
                        descripcion = reseñaDocument.getString("descripcion") ?: "",
                        calificacion = reseñaDocument.getString("calificacion") ?: "",
                        revisar = reseñaDocument.getBoolean("revisar") ?: false)

                    listaReseñas.add(reseñaData)
                }
                usuarioReseñas = listaReseñas
            }
            .addOnFailureListener { e ->
                // Manejar errores al realizar la consulta
                e.printStackTrace()
            }
   // }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv1
    ) {
        LazyColumn(){
            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    text = "Perfil",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            item {
                Spacer(modifier = modifier.height(10.dp))
            }
            item {
                //Tarjeta con la informacion del usuario--------------------------------------------
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Trv1
                    ),
                    border = CardDefaults.outlinedCardBorder(),
                ) {
                    Row(modifier = modifier
                        .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Card(
                            modifier = modifier
                                .padding(10.dp)
                                .size(80.dp),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Image(
                                modifier = modifier.fillMaxSize(),
                                painter = rememberAsyncImagePainter(model = usuario.foto_perfil),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                modifier = modifier
                                    .padding(top = 5.dp)
                                    .fillMaxHeight(),
                                text = usuario.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,

                                )
                            Text(
                                text = "Se unió en ${usuario.fechaDeRegistro}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Icon(
                                modifier = modifier
                                    .clickable { navController.navigate(Pantalla.EditarPerfil.ruta) }
                                    .padding(vertical = 5.dp),
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            //Descripcion del usuario---------------------------------------------------------------
            if(usuario.descripcion != ""){
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp, vertical = 15.dp)
                            .fillMaxWidth(),
                        text = usuario.descripcion?: "",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else {

            }

            item {
                if (usuario.lugarDeOrigen != null){
                    Row (
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "",
                            tint = Color.White,
                        )
                        Text(
                            text = "Lugar de origen: ${usuario.lugarDeOrigen}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }
            item {
                Divisor()
            }
            item {
                if (usuarioReseñas.isEmpty()) {
                    Log.i("no hay reseñas", usuarioReseñas.size.toString())
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(horizontal = 25.dp)
                        ) {
                            Text(
                                modifier = modifier.fillMaxSize(),
                                text = "No hay reseñas",
                                style = MaterialTheme.typography.displaySmall,
                                textAlign = TextAlign.Left,
                                color = Color.White
                            )
                        }
                } else {
                    Log.i("Hay reseñas", usuarioReseñas.size.toString())
                        for (dataRes in usuarioReseñas) {
                            TarjetaReseñaUsuario(
                                reseña = dataRes, clave = dataRes.placeId, APIoApp = false, onDeleteClick = {},
                                navController = navController
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                        }
                }
            }
        }
    }
}
@Composable
fun TarjetaReseñaUsuario(reseña: reseña, modifier: Modifier = Modifier,
                  clave: String, APIoApp: Boolean, onDeleteClick: () -> Unit,
                  navController: NavController
) {
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Pantalla.Detalles.conArgs(reseña.placeId))}
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
                if(reseña.foto != null)  {
                    fotoDePerfilUsuarioo(url = reseña.foto)
                } else {
                    val usuario = usuarioPrueba
                    Log.i("entro", "no hay iamgen")
                    Image(
                        painter = rememberAsyncImagePainter(model = usuario.foto_perfil),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                }

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
                Text(
                    modifier = modifier.fillMaxWidth(0.75f),
                    text = "${reseña.calificacion}/5",
                    textAlign = TextAlign.Right,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = "",
                    tint = Color.Yellow
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

@Composable
fun fotoDePerfilUsuarioo(url: String) {
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(80.dp, 80.dp)
            .clip(CircleShape)
            .padding(13.dp)
    )
}