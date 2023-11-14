package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Data.Pregunta
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv4
import com.example.trovare.ui.theme.Trv5
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.api.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Question(val pregunta: String = "", val respuesta: String = "", val id: String = "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreguntasAdmin(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    var questions by remember { mutableStateOf(emptyList<Question>()) }
    var expandedQuestionIndex by remember { mutableStateOf(-1) }
    var isAddingNewQuestion by remember { mutableStateOf(false) }

    var textoPregunta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoRespuesta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    val keyboardOptionsTexto: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)

    var isLoading by remember { mutableStateOf(true) }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        Log.i("FaqScreen", "LaunchedEffect: Obtaining questions from Firestore")
        try {
            val questionsSnapshot = firestore.collection("FAQS").get().await()
            questions = questionsSnapshot.toObjects(Question::class.java)
            Log.i("FaqScreen", "Questions obtained successfully: $questions")
        } catch (e: Exception) {
            Log.i("FaqScreen", "Error obtaining questions from Firestore", e)
        }finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ) {
                FloatingActionButton(
                    onClick = {
                        isAddingNewQuestion = !isAddingNewQuestion
                    },
                    containerColor = Trv4,
                    modifier = Modifier
                        .padding(start = 300.dp, bottom = 50.dp)
                        .size(60.dp)
                ) {
                    Icon(
                        imageVector = if (isAddingNewQuestion) Icons.Filled.Cancel else Icons.Filled.Add,
                        contentDescription = "",
                        tint = Trv5,
                        modifier = Modifier
                            .size(40.dp)
                    )
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
            Column ( modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Añadir esta línea para el desplazamiento vertical
                ){
                if (isLoading) {
                    // Muestra el CircularProgressIndicator mientras se cargan las preguntas
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Transparent)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Center),
                            color = Color.White
                        )
                    }
                } else{
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .weight(.5f) // Esto permitirá que LazyColumn ocupe el espacio vertical restante disponible
                        .padding(bottom = 16.dp)) {
                        item {
                            TituloAdmin(titulo = "EDITAR PREGUNTAS")
                        }
                        item {
                            Divisor(modifier = modifier.padding(15.dp))
                        }
                        items(questions.size) { index ->
                            val question = questions[index]

                            TarjetaPreguntas(
                                question = question,
                                expanded = index == expandedQuestionIndex,
                                onExpandClick = {
                                    expandedQuestionIndex =
                                        if (expandedQuestionIndex == index) -1 else index
                                },
                                onDeleteClick = {
                                    // Eliminar la pregunta de Firestore
                                    firestore.collection("FAQS").document(question.id.toString())
                                        .delete()
                                    questions = questions.filterNot { it.id == question.id }
                                },
                                modifier = modifier.padding(top = 20.dp),
                                navController = navController
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))
                if (isAddingNewQuestion) {
                    // Campos de "Nueva Pregunta" y "Nueva Respuesta"
                    Column {
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

                        Spacer(modifier = Modifier.height(8.dp))

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

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    if (textoPregunta.text.isNotBlank() && textoPregunta.text.isNotBlank()) {
                                        // Agregar nueva pregunta a Firestore
                                        val newQuestionDocument =
                                            firestore.collection("FAQS").document()
                                        newQuestionDocument.set(
                                            Question(
                                                pregunta = textoPregunta.text,
                                                respuesta = textoRespuesta.text,
                                                id = newQuestionDocument.id
                                            )
                                        )
                                        questions = questions + Question(
                                            pregunta = textoPregunta.text,
                                            respuesta = textoRespuesta.text,
                                            id = newQuestionDocument.id
                                        )
                                        textoPregunta = TextFieldValue("", TextRange(0, 0))
                                        textoRespuesta = TextFieldValue("", TextRange(0, 0))
                                        isAddingNewQuestion = false
                                    }
                                },
                                modifier = Modifier
                                    .heightIn(min = 56.dp)
                                    .padding(start = 8.dp, end = 50.dp)
                            ) {
                                Text("Agregar pregunta")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPreguntas(
    question: Question,
    expanded: Boolean,
    onExpandClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        colors = CardDefaults
            .cardColors(containerColor = Trv1),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onExpandClick() }
            ) {
                Icon(
                    modifier = Modifier
                        .padding(13.dp),
                    imageVector = Icons.Filled.QuestionMark,
                    contentDescription = "",
                    tint = Color.White,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(top = 8.dp),
                        text = question.pregunta,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
                Icon(
                    modifier = Modifier
                        .padding(13.dp)
                        .clickable {
                            navController.navigate(Pantalla.EditarPreguntas.ruta + "/${question.id}")
                            Log.i("pregunta2", question.id)
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
                },
                textoConfirmar = "Borrar Pregunta",
                titulo = "Borrar Pregunta",
                texto = "¿Quieres borrar la pregunta frecuente?",
                icono = Icons.Filled.DeleteForever,
                colorConfirmar = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = question.respuesta,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 50.dp, end = 50.dp)
                        .height(50.dp)
                )
            }
        }
    }
}