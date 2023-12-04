package com.example.trovare.ui.theme.Pantallas.Ingreso

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mail
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasena(
    modifier: Modifier = Modifier,
    navController: NavController
){
    // Definimos las variables
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isErrorC by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var guardadoExitoso by remember { mutableStateOf(false) }
    var textoCorreo by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    val keyboardOptionsCorreo: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done)

    LaunchedEffect(guardadoExitoso){

        if(guardadoExitoso) {
            delay(1000)
            guardadoExitoso = false
            navController.popBackStack()
        }
    }

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
                    .padding(25.dp)
                    .wrapContentSize(),
                colors = CardDefaults.cardColors(Trv8)
            ) {
                Column {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "REESTABLECER CONTRASEÑA",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        text = "¿Olvidaste tu contraseña? Ingresa tu correo electrónico asociado a la cuenta y enviaremos un código de verificación.",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                    // Correo ----------------------------------------------------------------------
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        isError = isErrorC,
                        value = textoCorreo,
                        onValueChange = { textoCorreo = it },
                        leadingIcon = { Icon(imageVector = Icons.Rounded.Mail, contentDescription = "") },
                        label = {
                            Text(
                                text = "Correo",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        placeholder = {
                            Text(
                                text = "Ejemplo@mail.com",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
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
                        keyboardOptions = keyboardOptionsCorreo,
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    //BOTON de enviar correo de verificaci[on
                    TextButton(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 10.dp),
                        onClick = {
                            // Realizamos las validadciones
                            if(textoCorreo.text.isBlank()){

                                Log.i("error_Recuperar", "campos imcompletos")
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Campos obligatorios no completados",
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
                            } else if(!isNetworkAvailable(context)){
                                Log.i("error conexión", "No hay conexión a internet")
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Error de conexión",
                                        duration = SnackbarDuration.Short
                                    )
                                }

                            } else{

                                auth.sendPasswordResetEmail(textoCorreo.text)
                                    .addOnCompleteListener{task ->

                                        if(task.isSuccessful){

                                            Log.d("Recuperar_Pass", "Correo enviado")
                                            isErrorC = false
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Verifica tu correo para reestablecer tu contraseña",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }

                                            guardadoExitoso = true

                                        }else{

                                            Log.d("Recuperar_Pass", "Correo no enviado")
                                            scope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = "Ingrese un correo válido",
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
                        )
                    ) {
                        Text(text = "Enviar")
                    }
                }
            }
        }
    }
}//Funciones auxiliares
@SuppressLint("ServiceCast")
private fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val network = connectivityManager.activeNetwork ?: return false
    val capabilities =
        connectivityManager.getNetworkCapabilities(network) ?: return false

    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
}