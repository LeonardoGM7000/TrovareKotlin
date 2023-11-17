package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Construction
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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Data.Pregunta
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv4
import com.example.trovare.ui.theme.Trv5
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.api.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    var isAddingNewQuestion by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    var textoPregunta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoRespuesta by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    val keyboardOptionsTexto: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)

    var isLoading by remember { mutableStateOf(true) }
    var borrarPregunta by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isErrorL: Int by rememberSaveable { mutableIntStateOf(0) }
    val maximoLetras = 30

    LaunchedEffect(Unit) {
        Log.i("FaqScreen", "LaunchedEffect: Obtaining questions from Firestore")
        try {
            val questionsSnapshot = firestore.collection("FAQS").get().await()
            questions = questionsSnapshot.toObjects(Question::class.java)
            Log.i("FaqScreen", "Questions obtained successfully: $questions")
        } catch (e: Exception) {
            Log.i("FaqScreen", "Error obtaining questions from Firestore", e)
        } finally {
            isLoading = false
        }
    }
    LaunchedEffect(borrarPregunta) {
        if (borrarPregunta) {
            delay(500)
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "Pregunta eliminada exitosamente",
                    duration = SnackbarDuration.Short
                )
            }
            borrarPregunta =false
        }
    }

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

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        }, snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ) {
                if (!isLoading) {
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
        }
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp) // Añadir esta línea para el desplazamiento vertical
            ) {
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
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 16.dp)
                    ) {
                        item {
                            TituloAdmin(titulo = "EDITAR PREGUNTAS")
                        }
                        item {
                            Divisor()
                        }
                        if (questions.isEmpty()) {
                            item {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = modifier
                                        .fillMaxWidth()
                                        .padding(top = 15.dp)
                                ) {
                                    Text(
                                        modifier = modifier.padding(start = 5.dp),
                                        text = "No hay preguntas",
                                        color = Color.White,
                                        fontSize = 25.sp,
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                }
                            }
                        } else {
                            items(questions.size) { index ->
                                val question = questions[index]

                                TarjetaPreguntas(
                                    question = question,
                                    onDeleteClick = {
                                        // Eliminar la pregunta de Firestore
                                        firestore.collection("FAQS").document(question.id)
                                            .delete()
                                        questions = questions.filterNot { it.id == question.id }
                                        borrarPregunta = true
                                    },
                                    modifier = modifier.padding(top = 8.dp),
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            if (isAddingNewQuestion) {
                // Campos de "Nueva Pregunta" y "Nueva Respuesta"
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(Trv1)
                        .padding(bottom = 16.dp)
                ) {
                    TituloAdmin(titulo = "Agregar pregunta")
                    Divisor()
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, start = 50.dp, end = 50.dp),
                        value = textoPregunta,
                        onValueChange = {
                            textoPregunta = it
                            isErrorL = 0
                            validarLetras(textoPregunta.text)
                        },
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
                        //parametro para mostrar eltipo de error
                        supportingText = {
                            isErrorL = isErrorL
                            if (isErrorL == 1) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Ingresa solo letras",
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else if (isErrorL == 2) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Máximo $maximoLetras carácteres",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
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
                        TextButton(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(start = 25.dp, end = 25.dp, bottom = 10.dp),
                            onClick = {
                                focusManager.clearFocus()
                                if (textoPregunta.text.isBlank() || textoRespuesta.text.isBlank()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Campos obligatorios no completados",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                } else if (isErrorL > 0) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Pregunta inválida",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                } else {
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
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Pregunta añadida exitosamente",
                                            duration = SnackbarDuration.Short
                                        )

                                    }
                                    isAddingNewQuestion = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Trv6,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Agregar pregunta")
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
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
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
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(cardSizeModifier) // Aplicar el modificador de tamaño aquí
            .padding(horizontal = 25.dp),
        colors = CardDefaults
            .cardColors(containerColor = Trv1)
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (expanded) Trv2
                    else Trv1
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(13.dp),
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    tint = Color.White,
                )
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth(0.64f)
                ) {
                    Text(
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
                textoConfirmar = "Borrar Pregunta",
                titulo = "Borrar Pregunta",
                texto = "¿Quieres borrar la pregunta frecuente?",
                icono = Icons.Filled.DeleteForever,
                colorConfirmar = Color.Red
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(visible = expanded) {
                if (expanded) {
                    Text(
                        modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                        text = question.respuesta,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White
                    )
                }
            }
        }
    }
}