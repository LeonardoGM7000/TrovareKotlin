package com.example.trovare.ui.theme.Pantallas

import android.text.format.DateUtils
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv7
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class Resena(val usuario: String, val puntuacion: Int, val texto: String, val tiempo: Int, val fotoPerfil: String)
@Composable
fun Detalles(
    modifier: Modifier = Modifier,
    placeId: String? = "",
    placesClient: PlacesClient,
    viewModel: TrovareViewModel,
    navController: NavController
){
    var favorito by rememberSaveable { mutableStateOf(false) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var numeroTelefono by rememberSaveable { mutableStateOf("") }
    var paginaWeb by rememberSaveable { mutableStateOf("") }
    var calificacion by rememberSaveable { mutableStateOf(-1.0) }
    var latLng by rememberSaveable { mutableStateOf(LatLng(0.0,0.0)) }
    var reseñasList by remember { mutableStateOf(mutableListOf<Resena>()) }


    fun obtenerResenas(apiKey: String, placeId: String) {
        val url = "https://maps.googleapis.com/maps/api/place/details/json?place_id=$placeId&key=$apiKey&language=es"

        val cliente = OkHttpClient()
        val solicitud = Request.Builder().url(url).build()

        cliente.newCall(solicitud).execute().use { respuesta ->
            if (respuesta.isSuccessful) {
                val cuerpoRespuesta = respuesta.body?.string()
                val datos = JSONObject(cuerpoRespuesta)

                if (datos.has("result")) {
                    val reseñas = datos.getJSONObject("result").optJSONArray("reviews")

                    if (reseñas != null) {

                        for (i in 0 until reseñas.length()) {
                            val reseña = reseñas.getJSONObject(i)
                            val usuario = reseña.optString("author_name", "Desconocido")
                            val puntuacion = reseña.optInt("rating", -1)
                            val texto = reseña.optString("text", "N/A")
                            val tiempo = reseña.optInt("time",-1)
                            val fotoDePerfil = reseña.optString("profile_photo_url","N/A")

                            reseñasList.add(Resena(usuario, puntuacion, texto, tiempo, fotoDePerfil))
                        }

                    } else {
                        Log.i("resena", "No se encontraron reseñas para este lugar.")
                    }
                } else {
                    Log.i("resena","No se encontraron detalles para el lugar.")
                }
            } else {
                Log.i("resena","Error en la solicitud: ${respuesta.message}")
            }
        }
        Log.i("resena",reseñasList.toString())
    }

    LaunchedEffect(key1 = Unit){
        viewModel.reiniciarImagen()
        viewModel.obtenerLugar(
            placesClient = placesClient,
            placeId = placeId?: "",
            nombre = { nombre = it?:"" },
            direccion = { direccion = it?:""},
            rating =  { calificacion = it?: -1.0},
            numeroTelefono = {numeroTelefono = it?: ""},
            paginaWeb = {paginaWeb = it?: ""},
            latLng = {latLng = it?: LatLng(0.0,0.0) },
        )
        GlobalScope.launch(Dispatchers.IO) {
            if (placeId != null) {
                obtenerResenas("AIzaSyDJBAeLUu6KewjD9hhDGNP8gCnshpG5y7c", placeId)
            }
        }

    }

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv7
    ) {
        LazyColumn(){
            //Tarjeta que muestra la imagen, botón de regreso y botón de agregar a favoritos--------
            item {
                Card(
                    modifier = modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .padding(start = 45.dp, top = 25.dp, end = 45.dp)
                        .aspectRatio(1F),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val imagen = viewModel.imagen.value

                        if (imagen != null) {
                            Image(
                                bitmap = imagen,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.FillBounds
                            )
                        } else {
                            Image(
                                modifier = modifier
                                    .fillMaxSize(),
                                painter = painterResource(id = R.drawable.image_placeholder),
                                contentDescription = ""
                            )
                        }
                        Row(
                            modifier = modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                        ) {
                            FloatingActionButton(
                                modifier = modifier
                                    .size(35.dp),
                                onClick = { navController.popBackStack() },
                                containerColor = Color.White,
                                shape = CircleShape
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                            Spacer(modifier = modifier.fillMaxWidth(0.86f))
                            FloatingActionButton(
                                modifier = modifier
                                    .size(35.dp),
                                onClick = { /*TODO*/ },
                                containerColor = Color.White,
                                shape = CircleShape
                            ) {
                                IconToggleButton(
                                    checked = favorito,
                                    onCheckedChange = { checked -> favorito = checked },
                                    colors = IconButtonDefaults.iconToggleButtonColors(
                                        containerColor = Color.White,
                                        checkedContainerColor = Color.White,
                                        contentColor = Color.Black,
                                        checkedContentColor = Color.Red
                                    )
                                ) {
                                    if (favorito) {
                                        Icon(
                                            imageVector = Icons.Rounded.Favorite,
                                            contentDescription = ""
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.FavoriteBorder,
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Divisor()
            }
            //Nombre del lugar y Rating del lugar---------------------------------------------------
            if(nombre != ""){
                item {
                    Row(
                        modifier = modifier.padding(horizontal = 25.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(0.7f),
                            text = nombre,
                            textAlign = TextAlign.Justify,
                            color = Color.White,
                            style = MaterialTheme.typography.displaySmall
                        )
                        Row( modifier = modifier
                            .fillMaxWidth(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = modifier.fillMaxWidth(0.75f),
                                text = "${calificacion}/5",
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
                    }
                }
            }
            //Direccion-----------------------------------------------------------------------------
            if(direccion != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            text = direccion,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            //Horario de apertura-------------------------------------------------------------------
            if(numeroTelefono != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.Phone,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            text = numeroTelefono,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            //Pagina web----------------------------------------------------------------------------
            if(paginaWeb != ""){
                item{
                    Row(modifier = modifier
                        .padding(horizontal = 45.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.Web,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            //modifier = modifier.clickable { context.startActivity(intent) },
                            text = paginaWeb,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            //Mapa con la ubicaci[on del lugar------------------------------------------------------
            item {
                Box(
                    Modifier
                        //.padding(horizontal = 25.dp)
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(latLng, 15f)
                    }
                    val estadoMarcador = rememberMarkerState(position = latLng)

                    if(latLng != LatLng(0.0,0.0)){
                        GoogleMap(
                            modifier = modifier
                                .matchParentSize(),
                            cameraPositionState = cameraPositionState,
                            properties = MapProperties(mapType = MapType.NORMAL, isBuildingEnabled = false),
                            uiSettings = MapUiSettings(
                                scrollGesturesEnabled = false,
                                scrollGesturesEnabledDuringRotateOrZoom = false,
                                zoomGesturesEnabled = false,
                                zoomControlsEnabled = false
                            )
                        ){
                            Marker(
                                state = estadoMarcador,
                                title = nombre,
                                //onClick = markerClick
                            )
                        }
                    }
                }
            }
            item {
                Divisor()
            }
            item {
                Text(
                    modifier = modifier
                        .padding(horizontal = 25.dp),
                    text = "Reseñas",
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Log.i("resena",reseñasList.size.toString())
            item {
                if (reseñasList.isNotEmpty()) {
                    reseñasList.forEach { reseña ->
                        TarjetaReseña(reseña = reseña)
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                } else {
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
    /*
    DisposableEffect(Unit) {
        onDispose {
            viewModel.reiniciarImagen()
        }
    }

     */
}

@Composable
fun TarjetaReseña(reseña: Resena, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }
    val cardSizeModifier = Modifier
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow,
            )
        )

    val tiempoActual = System.currentTimeMillis() // Tiempo actual en milisegundos
    val tiempoResena = TimeUnit.SECONDS.toMillis(reseña.tiempo.toLong()) // Convertir a milisegundos

    val tiempoTranscurrido = DateUtils.getRelativeTimeSpanString(tiempoResena, tiempoActual, DateUtils.MINUTE_IN_MILLIS)




    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(cardSizeModifier) // Aplicar el modificador de tamaño aquí
            .padding(horizontal = 25.dp),
    ) {
        Column(
            /*modifier = Modifier
                .background(
                    color = if (expanded) Trv2
                    else Trv1
                )*/
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {

                fotoDePerfilUsuario(url = reseña.fotoPerfil)

                    Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                ) {
                    Text(
                        text = reseña.usuario,
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
                    text = "${reseña.puntuacion}/5",
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
            AnimatedVisibility(visible = expanded) {
                if(expanded){

                    Text(
                        modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                        text = reseña.texto,
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
fun fotoDePerfilUsuario(url: String) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.size(80.dp, 80.dp).clip(CircleShape).padding(13.dp)
        )
    }
