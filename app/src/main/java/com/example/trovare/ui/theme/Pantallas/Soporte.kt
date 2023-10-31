package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipBorder
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.PantallasTrovare
import com.example.trovare.ui.theme.Data.listaDePreguntas
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.TrovareTheme
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Soporte(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {

            var resultoUtil by rememberSaveable { mutableStateOf("") }
            var textoComentario by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue("", TextRange(0, 7)))
            }

            LazyColumn() {
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Soporte",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        text = "Informa a soporte técnico",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Text(
                        modifier = modifier
                            .padding(horizontal = 55.dp, vertical = 15.dp),
                        text = "Para informar a soporte técnico puedes comunicarte a través de:",
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                item {
                    Card(
                        modifier = modifier
                            .padding(horizontal = 30.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(Trv3)
                    ) {
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Row(
                                modifier = modifier.padding(top = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = modifier.padding(5.dp),
                                    imageVector = Icons.Filled.Phone,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "55-5555-5555",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )

                            }
                            Row(
                                modifier = modifier.padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    modifier = modifier.padding(5.dp),
                                    imageVector = Icons.Filled.Mail,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                                Text(
                                    text = "Trovare@mail.com",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Black
                                )

                            }

                        }

                    }
                }
                item {
                    Divisor()
                }
                item {
                    Text(
                        modifier = modifier
                            .padding(horizontal = 55.dp, vertical = 15.dp)
                            .fillMaxSize(),
                        text = "¿Te resultó útil?",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        modifier = modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilterChip(
                            selected = resultoUtil == "Si",
                            onClick = { resultoUtil = "Si" },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF298538), containerColor = Trv1),
                            label = { Text(text = "Si") }
                        )
                        Spacer(modifier = modifier.padding(3.dp))
                        FilterChip(
                            selected = resultoUtil == "No",
                            onClick = { resultoUtil = "No" },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF853129), containerColor = Trv1),
                            label = { Text(text = "No") }
                        )
                    }
                }
                item {
                    Divisor()
                }
                item {
                    OutlinedTextField(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 25.dp, vertical = 15.dp),
                        value = textoComentario,
                        onValueChange = { textoComentario = it },
                        label = {
                            Text(
                                text = "Comentarios",
                                style = MaterialTheme.typography.labelSmall
                            )
                        },
                        textStyle = MaterialTheme.typography.labelSmall,
                        placeholder = {
                            Text(
                                text = "Escribe tus comentarios",
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
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(onSend = {}),
                    )
                }
                item {
                    Box(
                        modifier = modifier
                            .padding(horizontal = 25.dp)
                            .fillMaxWidth()
                    ){
                        TextButton(
                            modifier = modifier.fillMaxWidth(),
                            onClick = { /*TODO*/ },
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
}
/*
@Preview
@Composable
fun SoportePreview(){
    TrovareTheme {
        Soporte()
    }

}

 */