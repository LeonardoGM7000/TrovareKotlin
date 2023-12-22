package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var estrellass:Int = 0
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarComentario(
    modifier: Modifier = Modifier,
    navController: NavController, placeId: String?
) {
    var textoPregunta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoRespuesta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var isErrorL: Int by rememberSaveable { mutableIntStateOf(0) }
    val maximoLetras = 250

    // Estado para manejar la carga de la pregunta desde Firestore
    var preguntaActual by remember(placeId) { mutableStateOf("") }
    var respuestaActual by remember(placeId) { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }

    val keyboardOptionsTexto: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    fun validarLetras(text: String) {
        if (!(text.matches("[a-zA-ZÀ-ÿ ]*".toRegex()))) {
            Log.i("Error Caracter inválido", text)
            isErrorL = 1
        } else {
            if (text.length > maximoLetras) {
                isErrorL = 2
            }
        }
    }

    // Utilizar LaunchedEffect para cargar la pregunta una vez al ingresar a la pantalla
    LaunchedEffect(placeId) {
        // Simulación de carga de datos desde Firestore
        val preguntaRespuesta = loadComentarioFromFirestore(placeId!!)

        // Verificar si se pudo cargar la pregunta y la respuesta desde Firestore
        if (preguntaRespuesta != null) {
            preguntaActual = preguntaRespuesta.descripcion ?: ""
            textoPregunta = TextFieldValue(preguntaActual)

        }
    }

    LaunchedEffect(guardadoExitoso) {
        if (guardadoExitoso) {
            delay(1000)
            try {
                // Lógica de guardado en Firestore
                saveComentarioToFirestore(placeId!!, textoPregunta.text, estrellass.toString())

                // Restablecer el estado
                guardadoExitoso = false

                // Navegar de vuelta a la pantalla anterior
                navController.popBackStack()
            } catch (e: Exception) {
                Log.i("guardadoExitoso", e.toString())
            }
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if(textoPregunta.text.isBlank()){
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Campos obligatorios no completados",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }else{
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Reseña guardada exitosamente",
                                    duration = SnackbarDuration.Short
                                )
                            }
                            // Guardar la pregunta editada en Firestore
                            guardadoExitoso = true
                            // Navegar de vuelta a la pantalla anterior
                            //navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .padding(start = 250.dp, bottom = 50.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(Trv6, Color.White)
                ) {
                    Text(text = "Guardar")
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
                    TituloAdmin(titulo = "EDITAR RESEÑA")
                }
                item {
                    Divisor(modifier = Modifier.padding(top = 13.dp))
                }
                item {
                    RatingScreenn()
                }
                item {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(bottom = 15.dp, start = 50.dp, end = 50.dp),
                        value = textoPregunta,
                        onValueChange = {
                            textoPregunta = it
                            isErrorL = 0
                            validarLetras(textoPregunta.text)
                        },
                        label = {
                            Text(
                                text = "Reseña",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,

                        colors = TextFieldDefaults.colors(

                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            focusedContainerColor =  Trv1,
                            unfocusedContainerColor = Trv1,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        maxLines = 5,
                        keyboardOptions = keyboardOptionsTexto,
                        supportingText = {
                            isErrorL = isErrorL
                            if (isErrorL == 2) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Máximo $maximoLetras carácteres",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

data class ComentarioCalificacion(val descripcion: String?, val calificacion: String?)

suspend fun loadComentarioFromFirestore(preguntaId: String): ComentarioCalificacion? {
    return try {
        val firestore = FirebaseFirestore.getInstance()
        val auth: FirebaseAuth by lazy { Firebase.auth }
        val documentSnapshot = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
            .collection("comentarios").document(preguntaId).get().await()
        ComentarioCalificacion(
            descripcion  = documentSnapshot.getString("descripcion"),
            calificacion = documentSnapshot.getString("calificacion")
        )
    } catch (e: Exception) {
        null
    }
}


// Simulación de guardado de datos en Firestore
suspend fun saveComentarioToFirestore(
    preguntaId: String,
    nuevaPregunta: String,
    nuevaRespuesta: String
) {
    try {
        val auth: FirebaseAuth by lazy { Firebase.auth }
        val firestore = FirebaseFirestore.getInstance()
        val preguntaRef = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
            .collection("comentarios").document(preguntaId)
        preguntaRef.update(
            mapOf(
                "descripcion" to nuevaPregunta,
                "calificacion" to nuevaRespuesta,
                "fecha" to (System.currentTimeMillis() / 1000).toString()
            )
        ).await()
    } catch (e: Exception) {
        Log.i("savePregunta",e.toString())
    }
}

@Composable
fun RatingScreenn() {
    var rating by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 15.dp, start = 50.dp, end = 50.dp),
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar estrellas seleccionables
        StarRatingg(
            rating = rating,
            onRatingChanged = { newRating ->
                rating = newRating
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Log.i("calificacion lugar",rating.toString())
        estrellass = rating
        // Puedes incluir otros elementos aquí, como comentarios o botones de enviar.
    }
}
@Composable
fun StarRatingg(
    rating: Int,
    onRatingChanged: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 1..5) {
            val starIcon = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder
            StarIconn(
                icon = starIcon,
                onStarClicked = { onRatingChanged(i) },
                tint = if (i <= rating) Color.Yellow else Color.Gray
            )
        }
    }
}

@Composable
fun StarIconn(
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