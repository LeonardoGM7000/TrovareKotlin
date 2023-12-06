package com.example.trovare.ui.theme.Pantallas

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.itinerarioPrueba
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuenta(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    //Variables-------------------------------------------------------------------------------------
    //Guardar estado de errores---------------------------------------------------------------------
    var isErrorP by rememberSaveable { mutableStateOf(false) }
    var isErrorL: Int by rememberSaveable { mutableIntStateOf(0) }
    var isErrorLA: Int by rememberSaveable { mutableIntStateOf(0) }
    var isErrorC by rememberSaveable { mutableStateOf(false) }
    val minimoPassword = 8
    val maximoLetras = 20
    //Guardar estado de errores---------------------------------------------------------------------

    val auth = FirebaseAuth.getInstance()
    val firestore = Firebase.firestore
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var textoNombre by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoApellido by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoCorreo by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoPassword by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var passwordOculta by rememberSaveable { mutableStateOf(true) }
    var aceptarTyC by rememberSaveable { mutableStateOf(false) }
    var cuentaCreada by remember { mutableStateOf(false) }
    val keyboardOptionsTexto: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
    val keyboardOptionsCorreo: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
    val keyboardOptionsPassword: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)
    //Configuracion de colores para campos de texto-------------------------------------------------
    val colores = TextFieldDefaults.textFieldColors(
        containerColor = Trv8,
        focusedLabelColor = Color.White,
        unfocusedLabelColor = Color.White,
        cursorColor = Color.White,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = Color.White,
        errorCursorColor = MaterialTheme.colorScheme.error,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        errorLabelColor = MaterialTheme.colorScheme.error,
        errorSupportingTextColor = MaterialTheme.colorScheme.error,
    )

    fun validarLetras(text: String) {
        if(!(text.matches("[a-zA-ZÀ-ÿ ]*".toRegex()))){
            Log.i("Error Caracter inválido",text)
            isErrorL = 1
        } else {
            if(text.length > maximoLetras){
                isErrorL = 2
            }
        }
    }
    fun validarLetrasA(text: String) {
        if(!(text.matches("[a-zA-ZÀ-ÿ ]*".toRegex()))){
            Log.i("Error Caracter inválido",text)
            isErrorLA = 1
        } else {
            if(text.length > maximoLetras){
                isErrorLA = 2
            }
        }
    }
    fun validarCorreo(textoCorreo: String){
        isErrorC = !Patterns.EMAIL_ADDRESS.matcher(textoCorreo).matches()
    }
    fun validarPassword(text: String){
        Log.i("Error tamaño",text.length.toString())
        isErrorP = !(text.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=.*[a-z]).{8,}\$".toRegex()))
    }

    //Interfaz usuario------------------------------------------------------------------------------
    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
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
                    .fillMaxWidth()
                    .padding(25.dp)
                    .verticalScroll(state = scrollState),
                colors = CardDefaults.cardColors(Trv8)
            ) {
                Column {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "CREAR CUENTA",
                        style = MaterialTheme.typography.displayMedium
                    )
                    Icon(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .size(100.dp),
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "",
                        tint = Color.White
                    )
                    //Nombre----------------------------------------------------------------------------
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        isError = isErrorL != 0,
                        value = textoNombre,
                        onValueChange = { textoNombre = it
                            isErrorL = 0
                            validarLetras(textoNombre.text)
                        },
                        //parametro para mostrar eltipo de error
                        supportingText = {
                            isErrorL = isErrorL
                            if(isErrorL == 1){
                                Text(
                                    text = "Ingresa solo letras",
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else if(isErrorL == 2){
                                Text(
                                    text = "Máximo $maximoLetras carácteres",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "Nombre(s) *",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsTexto,
                    )
                    //Apellido--------------------------------------------------------------------------
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        isError = isErrorLA != 0,
                        value = textoApellido,
                        onValueChange = { textoApellido = it
                            isErrorLA = 0
                            validarLetrasA(textoApellido.text)},
                        //parametro para mostrar eltipo de error
                        supportingText = {
                            isErrorLA = isErrorLA
                            if(isErrorLA == 1){
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Ingresa solo letras",
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else if(isErrorLA == 2){
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Máximo $maximoLetras carácteres",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "Apellido(s) *",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsTexto,
                    )
                    //Correo----------------------------------------------------------------------------
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        isError = isErrorC,
                        value = textoCorreo,
                        onValueChange = { textoCorreo = it
                            validarCorreo(textoCorreo.text)},
                        supportingText = {
                            isErrorC = isErrorC
                            if (isErrorC) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Ingresa un correo válido.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        label = {
                            Text(
                                text = "Correo *",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Ejemplo@mail.com",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsCorreo,
                    )
                    //Contrasena------------------------------------------------------------------------
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        isError = isErrorP,
                        value = textoPassword,
                        onValueChange = { textoPassword = it
                            validarPassword(textoPassword.text)},
                        //parametro para mostrar eltipo de error
                        supportingText = {
                            isErrorP = isErrorP
                            if (isErrorP) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Ingresar al menos $minimoPassword caracteres, una mayúscula, una minúscula, un número y un caracter especial.",
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Justify
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordOculta = !passwordOculta }) {
                                val visibilityIcon =
                                    if (passwordOculta) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                                val description =
                                    if (passwordOculta) "Mostrar contraseña" else "Ocultar contraseña"
                                Icon(
                                    imageVector = visibilityIcon,
                                    contentDescription = description,
                                    tint = Color.White
                                )
                            }
                        },
                        visualTransformation = if (passwordOculta) PasswordVisualTransformation() else VisualTransformation.None,
                        label = {
                            Text(
                                text = "Contraseña *",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsPassword,
                    )
                    //Aceptar terminos y condiciones----------------------------------------------------
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            modifier = modifier.padding(start = 25.dp),
                            selected = aceptarTyC,
                            colors = RadioButtonDefaults.colors(selectedColor = Trv6),
                            onClick = { aceptarTyC = !aceptarTyC }
                        )
                        Text(
                            modifier = modifier.padding(end = 25.dp),
                            text = "Aceptar Términos y Condiciones",
                            style = MaterialTheme.typography.labelSmall,
                            textDecoration = TextDecoration.Underline,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = modifier.fillMaxHeight(0.6f))
                    //Boton registro--------------------------------------------------------------------
                    TextButton(
                        enabled = aceptarTyC,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 10.dp),
                        onClick = {
                            //Iniciar-------------------------------------------------------------------
                            if(textoNombre.text.isBlank() || textoApellido.text.isBlank() || textoCorreo.text.isBlank() || textoPassword.text.isBlank()){
                                Log.i("error", "campos incompletos")
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Completar todos los campos",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else if(isErrorL>0) {
                                Log.i("error campo nombre", textoNombre.text)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Nombre(s) inválido(s)",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else if(isErrorLA>0){
                                Log.i("error campo apellido", textoApellido.text)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Apellidos(s) inválido(s)",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else if(!Patterns.EMAIL_ADDRESS.matcher(textoCorreo.text).matches()){
                                Log.i("error correo", textoCorreo.text)
                                scope.launch {
                                    isErrorC = true
                                    snackbarHostState.showSnackbar(
                                        message = "Correo inválido",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else if(!isNetworkAvailable(context)) {
                                Log.i("error conexión", "No hay conexión a internet")
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Error de conexión",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else if(isErrorP) {
                                Log.i("error contraseña", textoPassword.text.length.toString())
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Contraseña inválida",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            } else {
                                auth.createUserWithEmailAndPassword(
                                    textoCorreo.text,
                                    textoPassword.text
                                ).addOnCompleteListener { task ->

                                        if (task.isSuccessful) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Cuenta creada exitosamente, ingresa a tu correo para validar tu cuenta",
                                                    duration = SnackbarDuration.Long
                                                )
                                                cuentaCreada = true
                                            }
                                            //Guardar datos firebase
                                            saveUserData(
                                                textoNombre.text,
                                                textoCorreo.text,
                                                firestore
                                            )

                                            val user = FirebaseAuth.getInstance().currentUser
                                            user?.sendEmailVerification()

                                        } else {

                                            scope.launch {
                                                isErrorC = true
                                                snackbarHostState.showSnackbar(
                                                    message = "El correo ingresado ya está asociado a otra cuenta",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Trv6,
                            contentColor = Color.White
                        ),
                    ) {
                        Text(text = "Registrarme")
                    }
                    LaunchedEffect(cuentaCreada) {
                        if (cuentaCreada) {
                            delay(1000)
                            navController.navigate(Pantalla.InicioDeSesion.ruta) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
//Guardar datos del usuario en firestore
private fun saveUserData(
    textoNombre: String,
    textoCorreo: String,
    firestore: FirebaseFirestore
) {
    // Crear un objeto para representar la información del usuario
    val userData = Usuario(textoNombre, null, obtenerFecha(), "", "México", null, mutableListOf())
    Log.i("cuenta", "Punto")
    firestore.collection("Usuario").document(textoCorreo).set(userData).addOnSuccessListener {
        Log.i("cuenta", "Datos guardados")
    }.addOnFailureListener {
        Log.i("cuenta", "Datos no guardados")
    }
}

@SuppressLint("ServiceCast")
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}

// Función para fecha
private fun obtenerFecha(): String{

    val calendario = GregorianCalendar()
    val fecha = calendario.time
    val formato = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    return formato.format(fecha)
}
