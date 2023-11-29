package com.example.trovare.ui.theme.Pantallas.Ingreso


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizarContrasena(
    modifier: Modifier = Modifier,
    navController: NavController
){

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
    var textoPasswrodNuevaContrasena by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoPasswrodConfirmacion by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var passwordOcultaNuevaContrasena by rememberSaveable { mutableStateOf(true) }
    var passwordOcultaConfirmacion by rememberSaveable { mutableStateOf(true) }
    val keyboardOptionsPassword: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done)

    var isErrorP by rememberSaveable { mutableStateOf(false) }
    val minimoPassword = 8

    fun validarPassword(text: String) {
        Log.i("Error tamaño",text.length.toString())
        isErrorP = !(text.matches("^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%^&*(),.?\":{}|<>])(?=.*[a-z]).{8,}\$".toRegex()))
    }

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
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
                    //Contraseña
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 10.dp),
                        isError = isErrorP,
                        value = textoPasswrodNuevaContrasena,
                        onValueChange = { textoPasswrodNuevaContrasena = it
                            validarPassword(textoPasswrodNuevaContrasena.text)},
                        //parametro para mostrar eltipo de error
                        supportingText = {
                            isErrorP = isErrorP
                            if (isErrorP) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Min $minimoPassword carácteres, una mayúscula, un número y carácter especial.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordOcultaNuevaContrasena = !passwordOcultaNuevaContrasena }) {
                                val visibilityIcon =
                                    if (passwordOcultaNuevaContrasena) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                                val description =
                                    if (passwordOcultaNuevaContrasena) "Mostrar contraseña" else "Ocultar contraseña"
                                Icon(
                                    imageVector = visibilityIcon,
                                    contentDescription = description,
                                    tint = Color.White
                                )
                            }
                        },
                        visualTransformation = if (passwordOcultaNuevaContrasena) PasswordVisualTransformation() else VisualTransformation.None,
                        label = {
                            Text(
                                text = "Contraseña",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsPassword,
                    )
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                        value = textoPasswrodConfirmacion,
                        onValueChange = { textoPasswrodConfirmacion = it },
                        trailingIcon = {
                            IconButton(onClick = { passwordOcultaConfirmacion = !passwordOcultaConfirmacion }) {
                                val visibilityIcon = if (passwordOcultaConfirmacion) Icons.Rounded.Visibility else Icons.Rounded.VisibilityOff
                                // Please provide localized description for accessibility services
                                val description = if (passwordOcultaConfirmacion) "Mostrar contraseña" else "Ocultar contraseña"
                                Icon(imageVector = visibilityIcon, contentDescription = description)
                            }
                        },
                        visualTransformation = if (passwordOcultaConfirmacion) PasswordVisualTransformation() else VisualTransformation.None,
                        label = {
                            Text(
                                text = "Confirmación",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        colors = colores,
                        singleLine = true,
                        keyboardOptions = keyboardOptionsPassword,
                    )
                    //Boton Reestablecer--------------------------------------------------------------------
                    TextButton(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 10.dp),
                        onClick = {

                        },
                        enabled = (textoPasswrodNuevaContrasena != TextFieldValue(""))&&!isErrorP,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Trv6,
                            contentColor = Color.White
                        ),
                    ) {
                        Text(text = "Reestablecer Contraseña")
                    }

                }
            }
        }
    }


}