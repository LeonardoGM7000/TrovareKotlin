package com.example.trovare.ui.theme.Pantallas.Ingreso

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.R
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6

@Composable
fun Bienvenida(
    modifier: Modifier = Modifier,
    navController: NavController
){
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv1
    ) {
        Card(
            modifier = modifier
                .padding(35.dp)
                .wrapContentSize(),
            colors = CardDefaults.cardColors(Trv1)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Titulo Trovare--------------------------------------------------------------------
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    text = "TROVARE",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge,
                    textAlign = TextAlign.Center,
                )
                //Texto Bienvenida------------------------------------------------------------------
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    text = "BIENVENIDO",
                    color = Color.White,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                )
                //Logo------------------------------------------------------------------------------
                Image(
                    modifier = modifier
                        .fillMaxHeight(0.35f)
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = ""
                )

                Spacer(
                    modifier = modifier
                        .fillMaxHeight(0.1f)
                )

                //Boton Iniciar Sesion--------------------------------------------------------------
                TextButton(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    onClick = {
                              navController.navigate(Pantalla.InicioDeSesion.ruta)//navegar a pantalla de inicio
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Trv6,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Iniciar sesión")
                }
                //Texto para enviar a página de registro--------------------------------------------
                Text(
                    modifier = modifier
                        .clickable {
                            navController.navigate(Pantalla.Registro.ruta)
                        },
                    text = "¿No tienes cuenta? Regístrate",
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline,
                )
                //Boton temporal para saltar el inicio de sesión (eliminar para la versión final)---
                TextButton(
                    modifier = modifier
                        .padding(bottom = 10.dp),
                    onClick = {
                        navController.navigate(Pantalla.NavegacionSecundaria.ruta)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "SALTAR")
                }
            }
        }
    }
}