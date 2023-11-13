package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv6
import com.example.trovare.ui.theme.Trv7

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPreguntas(
    modifier: Modifier = Modifier,
    navController: NavController
){

    var textoPregunta by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("", TextRange(0, 7))) }

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .background(Trv1)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {/*TODO*/},
                    modifier = Modifier
                        .padding(20.dp)
                        .clip(RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(Trv6,Color.White)
                ){
                    Text(text = "Guardar")
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
            LazyColumn(){
                item {
                    TituloAdmin(titulo = "EDITAR PREGUNTAS")
                }
                item {
                    Divisor(modifier = Modifier.padding(top = 13.dp))
                }
                item {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clip(RoundedCornerShape(20.dp)),
                        value = textoPregunta,
                        onValueChange = {textoPregunta = it},
                        label = {
                            Text(
                                text = "Pregunta",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White
                            )
                        },
                        textStyle = MaterialTheme.typography.labelMedium,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            containerColor = Trv7,
                            cursorColor = Color.White
                        )
                    )
                }
            }
        }
    }
}