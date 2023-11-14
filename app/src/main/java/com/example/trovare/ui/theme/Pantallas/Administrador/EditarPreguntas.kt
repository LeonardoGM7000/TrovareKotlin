package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPreguntas(
    modifier: Modifier = Modifier,
    navController: NavController, preguntaId: String?
) {
    var textoPregunta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoRespuesta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    // Estado para manejar la carga de la pregunta desde Firestore
    var preguntaActual by remember(preguntaId) { mutableStateOf("") }
    var respuestaActual by remember(preguntaId) { mutableStateOf("") }
    var guardadoExitoso by remember { mutableStateOf(false) }

    val keyboardOptionsTexto: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)

    // Utilizar LaunchedEffect para cargar la pregunta una vez al ingresar a la pantalla
    LaunchedEffect(preguntaId) {
        // Simulación de carga de datos desde Firestore
        val preguntaRespuesta = loadPreguntaFromFirestore(preguntaId!!)

        // Verificar si se pudo cargar la pregunta y la respuesta desde Firestore
        if (preguntaRespuesta != null) {
            preguntaActual = preguntaRespuesta.pregunta ?: ""
            textoPregunta = TextFieldValue(preguntaActual)

            respuestaActual = preguntaRespuesta.respuesta ?: ""
            textoRespuesta = TextFieldValue(respuestaActual)
        }
    }

    LaunchedEffect(guardadoExitoso) {
        if (guardadoExitoso) {
            try {
                // Lógica de guardado en Firestore
                savePreguntaToFirestore(preguntaId!!, textoPregunta.text, textoRespuesta.text)

                // Restablecer el estado
                guardadoExitoso = false

                // Navegar de vuelta a la pantalla anterior
                navController.popBackStack()
            } catch (e: Exception) {
            }
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        // Guardar la pregunta editada en Firestore
                        guardadoExitoso = true

                        // Navegar de vuelta a la pantalla anterior
                        navController.popBackStack()
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
                    TituloAdmin(titulo = "EDITAR PREGUNTA")
                }
                item {
                    Divisor(modifier = Modifier.padding(top = 13.dp))
                }
                item {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, start = 50.dp, end = 50.dp),
                        value = textoPregunta,
                        onValueChange = { textoPregunta = it },
                        label = {
                            Text(
                                text = "Pregunta",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            containerColor = Trv8,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = keyboardOptionsTexto,
                    )
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, start = 50.dp, end = 50.dp),
                        value = textoRespuesta,
                        onValueChange = { textoRespuesta = it },
                        label = {
                            Text(
                                text = "Respuesta",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White,
                            containerColor = Trv8,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White
                        ),
                        singleLine = true,
                        keyboardOptions = keyboardOptionsTexto,
                    )
                }
            }
        }
    }
}

data class PreguntaRespuesta(val pregunta: String?, val respuesta: String?)

suspend fun loadPreguntaFromFirestore(preguntaId: String): PreguntaRespuesta? {
    return try {
        val firestore = FirebaseFirestore.getInstance()
        val documentSnapshot = firestore.collection("FAQS").document(preguntaId).get().await()
        PreguntaRespuesta(
            pregunta = documentSnapshot.getString("pregunta"),
            respuesta = documentSnapshot.getString("respuesta")
        )
    } catch (e: Exception) {
        null
    }
}


// Simulación de guardado de datos en Firestore
suspend fun savePreguntaToFirestore(
    preguntaId: String,
    nuevaPregunta: String,
    nuevaRespuesta: String
) {
    try {
        val firestore = FirebaseFirestore.getInstance()
        val preguntaRef = firestore.collection("FAQS").document(preguntaId)
        preguntaRef.update(
            mapOf(
                "pregunta" to nuevaPregunta,
                "respuesta" to nuevaRespuesta
            )
        ).await()
    } catch (e: Exception) {
    }
}