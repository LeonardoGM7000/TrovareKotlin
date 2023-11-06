package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.example.trovare.ui.theme.Trv1

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun EditarPerfil(
    modifier: Modifier = Modifier,
    usuario: Usuario = usuarioPrueba,
){

    var textoNombre by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoPais by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    var textoInformacion by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done)

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = Trv1
    ) {
        LazyColumn(){
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
                        containerColor = Trv1
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
                                modifier = modifier.fillMaxSize(),
                                painter = painterResource(id = usuario.foto_perfil),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
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
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        containerColor = Trv1,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    ),
                    maxLines = 5,
                    keyboardOptions = keyboardOptions
                    )
            }
            item {
                OutlinedTextField(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                    value = textoNombre,
                    onValueChange = { textoNombre = it },
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
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        containerColor = Trv1,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    ),
                    maxLines = 5,
                    keyboardOptions = keyboardOptions
                )
            }
        }

    }
}

