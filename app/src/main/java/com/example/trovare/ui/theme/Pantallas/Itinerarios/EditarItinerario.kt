package com.example.trovare.ui.theme.Pantallas.Itinerarios

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.trovare.Data.Hora
import com.example.trovare.Data.Itinerario
import com.example.trovare.Data.Lugar
import com.example.trovare.Data.Usuario
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.CalendarTheme
import com.example.trovare.ui.theme.ClockTheme
import com.example.trovare.ui.theme.JosefinSans
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv11
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.ByteArrayOutputStream
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarItinerario(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: TrovareViewModel
){

    val usuario by viewModel.usuario.collectAsState()
    val itinerario by viewModel.itinerarioActual.collectAsState()
    var nombreItinerario by remember { mutableStateOf(itinerario.nombre) }
    var publico by rememberSaveable { mutableStateOf(false) }
    //var lugares by remember { mutableStateOf(itinerario.lugares) }
    val lista_lugares by viewModel.listaLugares.collectAsState()
    var lugares = lista_lugares
    val calendarState = rememberSheetState()
    val clockState = rememberSheetState()
    var indiceActual by remember{ mutableStateOf(0) }
    var listaVisible by remember{ mutableStateOf(true) }
    var mostrarBorrarDeItinerario by rememberSaveable { mutableStateOf(false) }

    viewModel.cargarLugar()


    val storage = FirebaseStorage.getInstance()

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Seleccionar imagen exitosa, ahora puedes subirla a Firebase Storage
                //uploadImageAndSaveReference(firestore, storage, userId ?: "", it, navController)
                cargarImagenitinerario(storage, it, itinerario)
            }
        }
    )
    //guardarImagen(viewModel)


    

    CalendarTheme {
        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Date{ fecha->
                CoroutineScope(Dispatchers.Default).launch {
                    listaVisible = false
                    viewModel.modificarFechaDeVisita(indiceActual = indiceActual, fechaNueva = fecha)
                    //itinerario.lugares = lugares?.sortedBy { it.fechaDeVisita }?.toMutableList()
                    itinerario.lugares = lugares.toMutableList()
                    lugares = itinerario.lugares!!
                    listaVisible = true

                    Log.d("Calendario_it", "${lugares.get(0).fechaDeVisita}")
                    Log.d("Calendario_it", "${itinerario.lugares}")
                    Log.d("Calendario_it", "${indiceActual}")
                }
            },
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true
            )
        )
    }

    ClockTheme {
        ClockDialog(
            state = clockState,
            selection = ClockSelection.HoursMinutes{hours, minutes ->
                CoroutineScope(Dispatchers.Default).launch {
                    listaVisible = false
                    viewModel.modificarHoraDeVisita(indiceActual = indiceActual, horaNueva = Hora(hora = hours, minuto = minutes))
                    //itinerario.lugares = lugares?.sortedBy { it.fechaDeVisita }?.toMutableList()
                    itinerario.lugares = lugares.toMutableList()
                    lugares = itinerario.lugares!!
                    listaVisible = true
                }
                //Log.d("testReloj", "${hours}:${minutes}")
            },
            config = ClockConfig(
                //is24HourFormat = true
            )
        )
    }
    
    Scaffold(
        topBar = {BarraSuperior(navController = navController)}
    ) {
        Surface(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            color = Trv1
        ) {


            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Editar Itinerario",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                item {
                    TextField(
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        value = nombreItinerario,
                        onValueChange = {nuevoNombre->
                            nombreItinerario = nuevoNombre
                            viewModel.setNombreItinerario(nuevoNombre)

                        },
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = JosefinSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            containerColor = Trv1,
                            focusedIndicatorColor = Trv1,
                            unfocusedIndicatorColor = Trv1,
                            cursorColor = Color.White,
                        )
                    )
                }
                item{
                    Card(
                        modifier = modifier
                            .padding(vertical = 10.dp)
                            .size(200.dp)
                            .aspectRatio(1F),
                    ){
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ){
                            if(lugares.isEmpty()) {
                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = painterResource(id = R.drawable.image_placeholder),
                                    contentDescription = ""
                                )
                            }else{

                                Image(
                                    modifier = modifier
                                        .fillMaxSize(),
                                    painter = rememberAsyncImagePainter(model = lugares.get(0).imagen),
                                    contentDescription = ""
                                )
                            }
                            FloatingActionButton(
                                modifier = modifier
                                    .padding(5.dp)
                                    .size(30.dp),
                                onClick = { getContent.launch("image/*")},
                                containerColor = Color.White,
                                shape = CircleShape
                            ){
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Público",
                            textAlign = TextAlign.Left,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = publico,
                            onCheckedChange = { publico = !publico },
                            thumbContent = {
                                if (publico){
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = ""
                                    )
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Trv11,
                                checkedThumbColor = Trv6,
                                checkedIconColor = Color.Black
                            )
                        )
                    }
                }
                item {
                    Divisor()
                }
                item {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .clickable {
                                //calendar State.show()
                                navController.navigate(Pantalla.AgregarLugarItinerario.ruta)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Trv3
                        )
                    ) {
                        Row (
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                modifier = modifier.padding(5.dp),
                                text = "Agregar lugar",
                                textAlign = TextAlign.Left,
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Icon(
                                modifier = modifier.padding(5.dp),
                                imageVector = Icons.Rounded.Place,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                }
                item {
                    Divisor()
                }
                if(lugares == null){
                    item {
                        Box(modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 25.dp)
                        ){
                            Text(
                                modifier = modifier.fillMaxSize(),
                                text = "No hay actividades en tu itinerario",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Left,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    if(listaVisible){
                        lugares!!.forEachIndexed { index, lugar ->
                            item {
                                Card(
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 25.dp, vertical = 5.dp)
                                        .size(100.dp)
                                        .clickable {
                                            navController.navigate(Pantalla.Detalles.conArgs(lugar.id))
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = Trv3
                                    )
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Card(
                                            modifier = modifier
                                                .padding(5.dp)
                                                .aspectRatio(1f),
                                        ) {

                                            if(lugar.imagen != null){

                                                Log.d("Editar_ImagenIt", "Entro al if")
                                                Log.d("Editar_ImagenIt", lugar.imagen!!)
                                                Image(
                                                    painter = rememberAsyncImagePainter(model = lugar.imagen),
                                                    modifier = modifier
                                                        .fillMaxSize(),
                                                    contentScale = ContentScale.FillBounds,
                                                    contentDescription = ""
                                                )
                                            } else{
                                                Image(
                                                    modifier = modifier
                                                        .fillMaxSize(),
                                                    painter = painterResource(id = R.drawable.image_placeholder),
                                                    contentDescription = ""
                                                )
                                            }
                                        }
                                        Column(
                                            modifier = modifier.fillMaxWidth(0.8f)
                                        ) {
                                            Text(
                                                text = lugar.nombreLugar,
                                                color = Color.Black,
                                                maxLines = 2
                                            )
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ){
                                                Row(
                                                    modifier = modifier
                                                        .clickable {
                                                            indiceActual = index
                                                            calendarState.show()
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ){
                                                    Icon(
                                                        imageVector = Icons.Rounded.CalendarToday,
                                                        contentDescription = "",
                                                        tint = Color.Black
                                                    )
                                                    Text(
                                                        text = if(lugar.fechaDeVisita == null) "" else lugar.fechaDeVisita.toString(),
                                                        color = Color.Black,
                                                        fontSize = 20.sp
                                                    )
                                                }
                                                Spacer(
                                                    modifier = modifier.padding(horizontal = 5.dp)
                                                )
                                                Row(
                                                    modifier = modifier
                                                        .clickable {
                                                            indiceActual = index
                                                            clockState.show()
                                                        },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ){
                                                    Icon(
                                                        imageVector = Icons.Rounded.AccessTime,
                                                        contentDescription = "",
                                                        tint = Color.Black
                                                    )
                                                    Text(
                                                        //text = if(lugar.horaDeVisita == null) "" else "${lugar.horaDeVisita!!.hora}:${lugar.horaDeVisita!!.minuto}",
                                                        text = lugar.horaDeVisita.toString(),
                                                        color = Color.Black,
                                                        fontSize = 20.sp
                                                    )
                                                }
                                            }
                                        }
                                        Column(
                                            modifier = modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            IconButton(
                                                onClick = {
                                                    indiceActual = index
                                                    mostrarBorrarDeItinerario = true
                                                    listaVisible = false
                                                    viewModel.borrarLugarActual(lugar)
                                                    lugares = itinerario.lugares!!
                                                    listaVisible = true


                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.DeleteForever,
                                                    contentDescription = "",
                                                    tint = Color.Black,
                                                )

                                            }
                                            IconButton(
                                                onClick = {
                                                    //TODO NAVEGAR A PANTALLA DE RUTAS
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Rounded.Route,
                                                    contentDescription = "",
                                                    tint = Color.Black,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


// Funciones auxiliares

private fun cargarImagenitinerario(

    storage: FirebaseStorage,
    imageUri: Uri,
    itinerario: Itinerario,
) {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storageRef: StorageReference = storage.reference.child("itinerario/user${auth.currentUser?.email.toString()}")

    // Subir la imagen a Firebase Storage
    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            // Obtener la URL de descarga de la imagen
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Guardar la referencia de la imagen en Firestore
                val lugar = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                    .collection("Itinerario").document(itinerario.id.toString())
                lugar.update("imagen", uri.toString())
                    .addOnSuccessListener {
                        // Éxito al guardar la referencia en Firestore
                        Log.i("Imagen_Itinerario", "Imagen cargada con éxito")
                    }
                    .addOnFailureListener { e ->
                        // Error al guardar la referencia en Firestore
                        Log.i("Imagen_Itinerario", "Error al guardar la imagen", e)
                    }
            }


        }
        .addOnFailureListener { e ->
            // Error al subir la imagen a Firebase Storage
            Log.i("firebase", "Error uploading image to Storage", e)
        }
}
