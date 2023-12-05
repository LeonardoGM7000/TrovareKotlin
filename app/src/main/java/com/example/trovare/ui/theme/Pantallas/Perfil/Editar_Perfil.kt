package com.example.trovare.ui.theme.Pantallas.Perfil

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv6
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfil(
    modifier: Modifier = Modifier,
    viewModel: TrovareViewModel,
    navController: NavController
){

    val usuario by viewModel.usuario.collectAsState()

    val storage = FirebaseStorage.getInstance()

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Seleccionar imagen exitosa, ahora puedes subirla a Firebase Storage
                //uploadImageAndSaveReference(firestore, storage, userId ?: "", it, navController)
                cargarImagenPerfil(storage, it, viewModel)
            }
        }
    )


    var textoNombre by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(usuario.nombre, TextRange(0, 7)))
    }
    var textoPais by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(usuario.lugarDeOrigen?:"México", TextRange(0, 7)))
    }
    var textoInformacion by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(usuario.descripcion?:"", TextRange(0, 7)))
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)


    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState)
        },
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            Card(
                modifier = modifier
                    .padding(25.dp)
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(Trv2)
            ) {
                //Cuerpo de Editar Perfil-----------------------------------------------------------
                LazyColumn(){
                    //titulo
                    item {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White,
                            text = "Editar Perfil",
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    item {
                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Trv2
                            ),
                        ) {
                            Row(modifier = modifier
                                .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center

                            ){
                                Card(
                                    modifier = modifier
                                        .padding(10.dp)
                                        .size(100.dp),
                                    shape = RoundedCornerShape(100.dp)
                                ) {

                                    Image(
                                        modifier = modifier
                                            .fillMaxSize()
                                            .clickable {
                                                getContent.launch("image/*")
                                            },
                                        painter = rememberAsyncImagePainter(model = usuario.foto_perfil),
                                        contentDescription = "",
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            }
                        }
                    }
                    //campo nmbre
                    item {
                        OutlinedTextField(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                            value = textoNombre,
                            onValueChange = { textoNombre = it },
                            label = {
                                Text(
                                    text = "Nombre(s)",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            textStyle = MaterialTheme.typography.labelSmall,
                            placeholder = {
                                Text(
                                    text = usuario.nombre,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedContainerColor = Trv2,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                            ),
                            singleLine = true,
                            keyboardOptions = keyboardOptions
                        )
                    }
                    item {
                        OutlinedTextField(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                            value = textoPais,
                            onValueChange = { textoPais = it },
                            label = {
                                Text(
                                    text = "Pais de Origen",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            textStyle = MaterialTheme.typography.labelSmall,
                            placeholder = {
                                Text(
                                    text = usuario.lugarDeOrigen?: "",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedContainerColor = Trv2,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                            ),
                            singleLine = true,
                            keyboardOptions = keyboardOptions
                        )
                    }
                    item {
                        OutlinedTextField(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                            value = textoInformacion,
                            onValueChange = { textoInformacion = it },
                            label = {
                                Text(
                                    text = "Descripción",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            textStyle = MaterialTheme.typography.labelSmall,
                            placeholder = {
                                Text(
                                    text = "Ingresar Descripción",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                focusedLabelColor = Color.White,
                                unfocusedLabelColor = Color.White,
                                focusedContainerColor = Trv2,
                                cursorColor = Color.White,
                                focusedIndicatorColor = Color.White,
                                unfocusedIndicatorColor = Color.White,
                            ),
                            maxLines = 5,
                            keyboardOptions = keyboardOptions
                        )
                    }
                    item {
                        Box(
                            modifier = modifier
                                .padding(horizontal = 25.dp)
                                .fillMaxWidth()
                        ){
                            TextButton(
                                modifier = modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                onClick = {
                                    //Muestra el mensaje de comentario enviado con éxito----------------
                                    scope.launch {
                                        editarPerfil(textoNombre.text,textoPais.text, textoInformacion.text, viewModel = viewModel)
                                        snackbarHostState.showSnackbar(
                                            message = "Cambios guardados con éxito",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Trv6,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = "Guardar")
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun editarPerfil(

    nombre: String,
    pais: String,
    descripcion: String,
    viewModel: TrovareViewModel
) {

    val auth = FirebaseAuth.getInstance()
    try {
        val firestore = FirebaseFirestore.getInstance()
        val usuario = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
        usuario.update(
            mapOf(
                "nombre" to nombre,
                "lugarDeOrigen" to pais,
                "descripcion" to descripcion
            )
        ).await()
        viewModel.obtenerDato()

    } catch (e: Exception) {
        Log.i("Editar_usuario",e.toString())
    }
}


private fun cargarImagenPerfil(

    storage: FirebaseStorage,
    imageUri: Uri,
    viewModel: TrovareViewModel,
) {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val storageRef: StorageReference = storage.reference.child("images/user${auth.currentUser?.email.toString()}")


    // Subir la imagen a Firebase Storage
    storageRef.putFile(imageUri)
        .addOnSuccessListener {
            // Obtener la URL de descarga de la imagen
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Guardar la referencia de la imagen en Firestore
                val usuario = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                usuario.update("foto_perfil", uri.toString())
                    .addOnSuccessListener {
                        // Éxito al guardar la referencia en Firestore
                        Log.i("Imagen_Perfil", "Imagen cargada con éxito")
                    }
                    .addOnFailureListener { e ->
                        // Error al guardar la referencia en Firestore
                        Log.i("Imagen_Perfil", "Error al guardar la imagen", e)
                    }
            }

            viewModel.obtenerDato()
        }
        .addOnFailureListener { e ->
            // Error al subir la imagen a Firebase Storage
            Log.i("firebase", "Error uploading image to Storage", e)
        }
}



