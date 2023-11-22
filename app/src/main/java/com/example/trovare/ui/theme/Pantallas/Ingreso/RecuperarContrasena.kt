package com.example.trovare.ui.theme.Pantallas.Ingreso

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv8

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasena(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var textoCorreo by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    val keyboardOptionsCorreo: KeyboardOptions =
        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done)

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
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        text = "¿Olvidaste tu contraseña? Ingresa tu correo electrónico asociado a la cuenta y enviaremos un código de verificación.",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
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
                                  navController.navigate(Pantalla.TokenRecuperarContrasena.ruta)
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
}