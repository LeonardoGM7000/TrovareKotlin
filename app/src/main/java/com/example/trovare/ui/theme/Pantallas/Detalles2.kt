package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

/*

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Perfil.fotoDePerfilUsuarioo
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv7
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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

data class ComentarioR(val Nombre:String, val fecha: String, val foto:String, val placeId: String,
                       var descripcion: String, var calificacion: String, val revisar: Boolean )
data class DatoRes(val placeId: String)
var estrellas : Int = 0
var effectkey = 0
var flagstate = mutableStateOf(0)
@OptIn(ExperimentalMaterial3Api::class)
data class Resena(val usuario: String, val puntuacion: Int, val texto: String, val tiempo: Int, val fotoPerfil: String)
@OptIn(ExperimentalMaterial3Api::class)
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
    var reseñasPropias by remember { mutableStateOf(mutableListOf<ComentarioR>()) }
    var fecha:Int = 0
    var foto:String = ""
    var NoexisteComPropios:Boolean = true
    val db = Firebase.firestore

    LaunchedEffect(effectKey){
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

                        // Crear una instancia de Resena con los datos obtenidos de Firestore
                        val nuevaReseña = ComentarioR(usuarioActual,fecha,foto,placeId.toString(),textoComentario, estrellas,false)
                        // Agregar la nueva reseña a la lista mutable reseñasPropias
                        reseñasPropias.add(nuevaReseña)
                    }
                }
            } else {
                println("El documento 'datos' en la colección 'Reseña' no existe en Firestore")
            }
        }.addOnFailureListener { e ->
            println("Error al obtener el documento 'datos' en Firestore: $e")
        }
    }


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

    var textoComentario by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var guardadoExitoso by remember { mutableStateOf(false) }
    var existeComentario by remember { mutableStateOf(false) }
    val auth: FirebaseAuth by lazy { Firebase.auth }
    val firestore = FirebaseFirestore.getInstance()
    var userId by remember { mutableStateOf<String?>(null) }
    var usuarioActual by remember { mutableStateOf("Desconocido")}
    var usuarioFoto by remember { mutableStateOf("")}
    val usuario by viewModel.usuario.collectAsState()

    // Verificar si hay un usuario autenticado
    val currentUser = auth.currentUser
    if (currentUser != null) {
        userId = currentUser.uid
    }
    val usuarioDocument = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
    // Obtener referencia a la subcolección "comentarios"
    val comentariosCollection = usuarioDocument.collection("comentarios")

    // ID del comentario a modificar (reemplaza con el ID real)
    val comentarioId = placeId.toString()
    // Obtener valores iniciales del comentario para mostrar modificar o nuevo
    comentariosCollection.document(comentarioId).get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // El comentario existe, obtener comentario previo
                textoComentario = TextFieldValue(documentSnapshot.getString("descripcion").toString())
                existeComentario = true
                estrellas = documentSnapshot.getString("calificacion")!!.toInt()
            } else {
                // El comentario no existe, iniciar en 0
                textoComentario = TextFieldValue("")
            }
        }
        .addOnFailureListener { e ->
            println("Error al verificar la existencia del comentario en Firestore: $e")
        }
    //}
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

    LaunchedEffect(flagstate){
        //flagstate = 1
        if(flagstate.value == 1) {
            scope.launch {
                snackbarHostState
                    .showSnackbar(
                        message = "Reseña borrada correctamente",
                        duration = SnackbarDuration.Short
                    )

            }

        }
        flagstate.value = 0;
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
            //Mapa con la ubicación del lugar------------------------------------------------------
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
                        TarjetaReseña(reseña = reseña, clave = placeId.toString(), APIoApp = false, onDeleteClick = {},
                            navController = navController)
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
            item {
                Divisor()
            }
            item {
                Text(
                    modifier = modifier
                        .padding(horizontal = 25.dp),
                    text = "Reseñas de Trovare",
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    style = MaterialTheme.typography.displaySmall
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            item{

                Log.i("Resenaspropiasss","$reseñasPropias")
                val reseñasFiltradas = reseñasPropias.filter { it.placeId == placeId.toString() }

// Mostrar las reseñas filtradas utilizando TarjetaReseña
                if (reseñasFiltradas.isNotEmpty()) {
                    reseñasFiltradas.forEach { reseña ->
                        // Crear la tarjeta de reseña para cada elemento de reseñasFiltradas
                        val aux = Resena(reseña.Nombre,reseña.calificacion.toInt(),reseña.descripcion,reseña.fecha.toInt(),reseña.foto)
                        TarjetaReseña(reseña = aux, onDeleteClick = {}, clave = placeId.toString(), APIoApp = false, navController = navController)
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
            item {
                Divisor()
            }
            if(!existeComentario){
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        text = "Deja tu Reseña",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    RatingScreen()
                }
                //Caja de comentarios---------------------------------------------------------------
                item {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        value = textoComentario,
                        onValueChange = { textoComentario = it },
                        label = {
                            Text(
                                text = "Reseña",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        placeholder = {
                            Text(
                                text = "Escribe tu reseña",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            containerColor = Trv1,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        maxLines = 5,
                        keyboardOptions = keyboardOptions
                        ,

                        )
                }

                firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                    .get()
                    .addOnSuccessListener { document ->
                        // Verificar si el documento existe y contiene el campo "name"
                        if (document.exists()) {
                            usuarioActual = document.getString("nombre") ?: "Desconocido"
                            usuarioFoto = document.getString("foto_perfil") ?: ""
                        }
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al realizar la consulta
                    }

                item {
                    Box(
                        modifier = modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth()
                    ){
                        TextButton(
                            enabled = textoComentario != TextFieldValue(""),
                            modifier = modifier.fillMaxWidth(),
                            onClick = {
                                Log.i("estrellas", estrellas.toString())
                                if (userId != null) {
                                    Log.i("activo",userId.toString())
                                    // Obtener referencia al documento del usuario activo
                                    val usuarioDocument = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                                    //obtener referencia a documento de reseñas
                                    val resenasCollection = firestore.collection("Reseña")
                                    val resenasTrovareMap = resenasCollection.document("datos")
                                    // Obtener referencia a la subcolección "comentarios"
                                    val comentariosCollection = usuarioDocument.collection("comentarios")

                                    // ID del comentario a modificar (reemplaza con el ID real)
                                    val comentarioId = placeId.toString()
                                    val aux = textoComentario.text
                                    Log.i("descripción",aux)

                                    // Actualizar el valor del campo en Firestore o crearlo en la coleccion de Usuario
                                    comentariosCollection.document(comentarioId).get()
                                        .addOnSuccessListener { documentSnapshot ->
                                            if (!documentSnapshot.exists()) {
                                                // El comentario no existe, crearlo
                                                val comentarioData = ComentarioR(usuarioActual,(System.currentTimeMillis() / 1000).toString(), usuarioFoto,
                                                    placeId.toString(),aux, estrellas.toString(),false)
                                                comentariosCollection.document(comentarioId).set(comentarioData)
                                                    .addOnSuccessListener {
                                                        flagstate.value = 1;
                                                    }
                                                    .addOnFailureListener { e ->
                                                        println("Error al crear el comentario en Firestore: $e")
                                                    }
                                            }
                                        }
                                        .addOnFailureListener { e ->
                                            println("Error al verificar la existencia del comentario en Firestore: $e")
                                        }
                                    // val resenasActuales = resenasTrovareMap["dataReseñas"] as? List<*>
                                    val dataRes = ComentarioR(usuarioActual,(System.currentTimeMillis() / 1000).toString(),usuarioFoto,placeId.toString(),aux, estrellas.toString(),false)
                                    //Agregar datos a la lista mutable de resenasPropias
                                    reseñasPropias.add(dataRes)
                                    Log.i("ReseñasCreada","$reseñasPropias")

                                    resenasTrovareMap.get().addOnSuccessListener { documentSnapshot ->
                                        if (documentSnapshot.exists()) {
                                            val datosActuales = documentSnapshot.data

                                            datosActuales?.let {
                                                val campoDataReseñas = it["dataReseñas"] as? MutableList<ComentarioR> ?: mutableListOf()
                                                campoDataReseñas.add(dataRes)

                                                // Actualizar el campo dataReseñas en el documento
                                                resenasTrovareMap.update("dataReseñas", campoDataReseñas)
                                                    .addOnSuccessListener {
                                                        println("Datos de reseñasPropias agregados correctamente a dataReseñas en Firestore")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        println("Error al agregar datos de reseñasPropias a dataReseñas en Firestore: $e")
                                                    }
                                            }
                                        } else {
                                            println("El documento 'datos' en la colección Reseña no existe en Firestore")
                                        }
                                    }.addOnFailureListener { e ->
                                        println("Error al obtener el documento 'datos' en Firestore: $e")
                                    }

                                } else {
                                    Log.i("activo null", userId.toString())
                                }
                                navController.popBackStack() // Regresa a la pantalla anterior rápidamente
                                navController.navigate(Pantalla.Detalles.conArgs(placeId.toString())) // Navega de nuevo a la pantalla actual
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Trv6,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = "Enviar")
                        }
                    }

                }
            } else {
                //hay comentario del usuario y lo puede modificar
                //agregar foto
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        text = "Modifica tu comentario",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                item {
                    comentariosCollection.document(comentarioId).get()
                        .addOnSuccessListener { documentSnapshot ->
                            if (documentSnapshot.exists()) {
                                // El comentario existe, obtener comentario previo
                                textoComentario = TextFieldValue(documentSnapshot.getString("descripcion").toString())
                                existeComentario = true
                                estrellas = documentSnapshot.getString("calificacion")!!.toInt()
                                usuarioActual = documentSnapshot.getString("nombre").toString()
                                fecha = documentSnapshot.getString("fecha")!!.toInt()
                                foto = usuario.foto_perfil.toString()
                            } else {
                                // El comentario no existe, iniciar en 0
                                textoComentario = TextFieldValue("")
                            }
                        }
                        .addOnFailureListener { e ->
                            println("Error al verificar la existencia del comentario en Firestore: $e")
                        }
                    val detalleComentario = Resena(usuarioActual,estrellas,
                        textoComentario.text,fecha,foto)

                    TarjetaReseña(reseña = detalleComentario,
                        onDeleteClick = {
                            // Eliminar resena
                            firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                                .collection("comentarios").document(placeId.toString())
                                .delete()

                            val resenasTrovareMapRef = firestore.collection("Reseña").document("datos")
                            val reseñaAEliminar = reseñasPropias.find { it.placeId == placeId.toString() && it.Nombre == usuarioActual }

                            reseñaAEliminar?.let {
                                reseñasPropias.remove(it)
                            }
                            resenasTrovareMapRef.get().addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val datosActuales = documentSnapshot.data

                                    datosActuales?.let {
                                        val campoDataReseñas = it["dataReseñas"] as? MutableList<HashMap<String, Any>>

                                        campoDataReseñas?.let { reseñasList ->
                                            val updatedReseñas = reseñasList.filterNot { reseña ->
                                                reseña["placeId"] == placeId.toString() && reseña["nombre"] == usuarioActual
                                            }

                                            // Actualizar el campo dataReseñas en el documento
                                            resenasTrovareMapRef.update("dataReseñas", updatedReseñas)
                                                .addOnSuccessListener {
                                                    println("Reseña con placeId eliminada correctamente en Firestore")
                                                    flagstate.value=1;
                                                }
                                                .addOnFailureListener { e ->
                                                    println("Error al eliminar la reseña en Firestore: $e")
                                                }
                                        }
                                    }
                                } else {
                                    println("El documento 'datos' en la colección 'Reseña' no existe en Firestore")
                                }
                            }.addOnFailureListener { e ->
                                println("Error al obtener el documento 'datos' en Firestore: $e")
                            }
                        },
                        clave = placeId.toString(), APIoApp = true, navController = navController)
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
        }
    }
}

@Composable
fun TarjetaReseña(reseña: Resena, modifier: Modifier = Modifier,
                  clave: String, APIoApp: Boolean,    onDeleteClick: () -> Unit,
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
    val tiempoResena = TimeUnit.SECONDS.toMillis(reseña.tiempo.toLong()) // Convertir a milisegundos

    val tiempoTranscurrido = DateUtils.getRelativeTimeSpanString(tiempoResena, tiempoActual, DateUtils.MINUTE_IN_MILLIS)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(cardSizeModifier) // Aplicar el modificador de tamaño aquí
            .padding(horizontal = 25.dp),
    ) {
        Column(
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {

                fotoDePerfilUsuarioo(url = reseña.fotoPerfil)

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
                    Column (
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                    ) {
                        Text(
                            modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                            text = reseña.texto,
                            textAlign = TextAlign.Justify,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        if(APIoApp){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(13.dp)
                                        .clickable {
                                            navController.navigate(Pantalla.EditarComentario.ruta + "/${clave}")
                                        },
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                                Icon(
                                    modifier = Modifier
                                        .padding(13.dp)
                                        .clickable {
                                            mostrarBorrarCuenta = true
                                        },
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "",
                                    tint = Color.White,
                                )
                            }
                            VentanaDeAlerta(
                                mostrar = mostrarBorrarCuenta,
                                alRechazar = { mostrarBorrarCuenta = false },
                                alConfirmar = { //Necesita eliminar la pregunta
                                    onDeleteClick()
                                    mostrarBorrarCuenta = false
                                },
                                textoConfirmar = "Borrar Reseña",
                                titulo = "Borrar Reseña",
                                texto = "¿Quieres borrar la Reseña?",
                                icono = Icons.Filled.DeleteForever,
                                colorConfirmar = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RatingScreen() {
    var rating by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar estrellas seleccionables
        StarRating(
            rating = rating,
            onRatingChanged = { newRating ->
                rating = newRating
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Log.i("calificacion lugar",rating.toString())
        estrellas = rating
        // Puedes incluir otros elementos aquí, como comentarios o botones de enviar.
    }
}

@Composable
fun StarRating(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..5) {
            val starIcon = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder
            StarIcon(
                icon = starIcon,
                onStarClicked = { onRatingChanged(i) },
                tint = if (i <= rating) Color.Yellow else Color.Gray
            )
        }
    }
}

@Composable
fun StarIcon(
    icon: ImageVector,
    onStarClicked: () -> Unit,
    tint: Color = Color.Gray
) {
    Icon(
        imageVector = icon,
        contentDescription = "Estrella",
        tint = tint,
        modifier = Modifier
            .size(40.dp)
            .clickable { onStarClicked() }
    )
}

 */


//__________________________________________________________________________________________________



/*
val usuario by viewModel.usuario.collectAsState()

var favorito by rememberSaveable { mutableStateOf(false) }
var eraFavoritoBand = false

var nombre by rememberSaveable { mutableStateOf("") }
var direccion by rememberSaveable { mutableStateOf("") }
var numeroTelefono by rememberSaveable { mutableStateOf("") }
var paginaWeb by rememberSaveable { mutableStateOf("") }
var calificacion by rememberSaveable { mutableStateOf(-1.0) }
var latLng by rememberSaveable { mutableStateOf(LatLng(0.0,0.0)) }
var resenasList by remember { mutableStateOf(mutableListOf<Resena>()) }
var reseñasPropias by remember { mutableStateOf(mutableListOf<ComentarioR>()) }
var fecha:Int = 0
var foto:String = ""
var NoexisteComPropios:Boolean = true
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
                        resenasList.add(Resena(usuario, puntuacion, texto, tiempo, fotoDePerfil))
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
    Log.i("resena",resenasList.toString())
}
var textoComentario by rememberSaveable(stateSaver = TextFieldValue.Saver) {
    mutableStateOf(TextFieldValue("", TextRange(0, 7)))
}
val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)
val scope = rememberCoroutineScope()
val snackbarHostState = remember { SnackbarHostState() }
var guardadoExitoso by remember { mutableStateOf(false) }
var existeComentario by remember { mutableStateOf(false) }
val auth: FirebaseAuth by lazy { Firebase.auth }
val firestore = FirebaseFirestore.getInstance()
var userId by remember { mutableStateOf<String?>(null) }
var usuarioActual by remember { mutableStateOf("Desconocido") }
var usuarioFoto by remember { mutableStateOf("") }




// Verificar si hay un usuario autenticado
val currentUser = auth.currentUser
if (currentUser != null) {
    userId = currentUser.uid
}
val usuarioDocument = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
// Obtener referencia a la subcolección "comentarios"
val comentariosCollection = usuarioDocument.collection("comentarios")
// ID del comentario a modificar (reemplaza con el ID real)
val comentarioId = placeId.toString()
// Obtener valores iniciales del comentario para mostrar modificar o nuevo
comentariosCollection.document(comentarioId).get()
.addOnSuccessListener { documentSnapshot ->
    if (documentSnapshot.exists()) {
        // El comentario existe, obtener comentario previo
        textoComentario = TextFieldValue(documentSnapshot.getString("descripcion").toString())
        existeComentario = true
        estrellas = documentSnapshot.getString("calificacion")!!.toInt()
    } else {
        // El comentario no existe, iniciar en 0
        textoComentario = TextFieldValue("")
    }
}
.addOnFailureListener { e ->
    println("Error al verificar la existencia del comentario en Firestore: $e")
}
//}
LaunchedEffect(key1 = Unit){
    eraFavoritoBand = false
    coroutineScope {
        //checar si es lugar favorito
        if(usuario.favoritos.isNotEmpty()){
            val hayCoincidencia = usuario.favoritos!!.any { lugar -> lugar.id == placeId }
            if(hayCoincidencia){
                favorito = true
                eraFavoritoBand = true
                Log.d("testFavorito","es lugar favorito")
            } else {
                Log.d("testFavorito","no es lugar favorito")
            }
        } else {
            Log.d("testFavorito","no hay lugares favoritos agregados")
        }

    }

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
    com.example.trovare.Api.obtenerResenas(placeId!!, resenasList)
}

 */

//--------------------------------------------------------------------------------------------------

/*


//import com.example.trovare.R
import android.text.format.DateUtils
import android.util.Log
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Question
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
                    foto = usuario.foto_perfil!!,
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
    Card(
        modifier = Modifier
            .padding(10.dp)
            .size(50.dp),
        shape = RoundedCornerShape(100.dp)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = rememberAsyncImagePainter(model = url),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )
    }
}

 */