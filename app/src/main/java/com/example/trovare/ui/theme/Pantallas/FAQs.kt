package com.example.trovare.ui.theme.Pantallas

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trovare.ui.theme.TrovareTheme
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.trovare.PantallasTrovare
import com.example.trovare.ui.theme.Data.Pregunta
import com.example.trovare.ui.theme.Data.listaDePreguntas
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv3




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQS(
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
        bottomBar = {
            BarrInferior()
        },
    ) { it ->

        //Cuerpo FAQS-------------------------------------------------------------------------------
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            LazyColumn(){
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "FAQs",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                items(listaDePreguntas){
                    TarjetaPregunta(pregunta = it)
                }
                item {
                    Divisor()
                }
                item {
                    Card(
                        modifier = modifier
                            .wrapContentSize()
                            .padding(start = 25.dp, end = 25.dp, bottom = 15.dp)
                            .clickable(
                                indication = null,
                                interactionSource = NoRippleInteractionSource()
                            ) { navController.navigate(PantallasTrovare.Soporte.name) },
                        colors = CardDefaults.cardColors(
                            containerColor = Trv1
                        ),
                    ) {
                        Row(modifier = modifier){
                            Icon(
                                modifier = modifier
                                    .padding(horizontal = 15.dp, vertical = 5.dp),
                                imageVector = Icons.Filled.Help,
                                contentDescription = "",
                                tint = Color.White,
                            )

                            Text(
                                text = "Soporte",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarrInferior(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Trv3
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Servicio al cliente",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
            Text(
                text = "5555-5555",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
        }

    }
}



@Composable
fun TarjetaPregunta(
    modifier: Modifier = Modifier,
    pregunta: Pregunta
) {
    var expanded by remember { mutableStateOf(false) }

    // Definir la animación de tamaño de la tarjeta
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
            .padding(horizontal = 25.dp, vertical = 15.dp)
            .then(cardSizeModifier) // Aplicar el modificador de tamaño aquí
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource()
            ) { expanded = !expanded },


        colors = CardDefaults.cardColors(
            containerColor = Trv1
        ),
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (expanded) Trv2
                    else Trv1
                )
        ) {
            Row(modifier = modifier.fillMaxWidth()) {
                Icon(
                    modifier = modifier
                        .padding(15.dp),
                    imageVector = Icons.Filled.Info,
                    contentDescription = "",
                    tint = Color.White,
                )

                Text(
                    modifier = modifier
                        .padding(1.dp)
                        .fillMaxWidth(0.83F)
                    ,
                    text = stringResource(id = pregunta.pregunta),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                BotonPregunta(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            if (expanded) {
                Text(
                    modifier = modifier.padding(start = 20.dp, end = 20.dp, bottom = 15.dp),
                    text = stringResource(id = pregunta.respuesta),
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun BotonPregunta(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    IconButton(
        modifier = modifier,
        onClick = onClick

    ){
        Icon(
            imageVector = if(expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = "",
            tint = Color.White
        )
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TrovareTheme {
        FAQS()
    }
}

 */