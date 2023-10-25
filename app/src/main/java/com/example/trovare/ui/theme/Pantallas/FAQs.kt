package com.example.trovare.ui.theme.Pantallas

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trovare.ui.theme.TrovareTheme
//import androidx.compose.material.icons.filled.ExpandMore
//import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.dimensionResource
import com.example.trovare.R
import com.example.trovare.ui.theme.Data.Pregunta
import com.example.trovare.ui.theme.Data.listaDePreguntas


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FAQS(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            barraSuperior()
        }
    ) { it ->
        CuerpoFAQS(padding = it)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun barraSuperior(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(Color(red = 25, green = 27, blue = 26)),
        navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        title = {
        }
    )
}

@Composable
fun CuerpoFAQS(padding: PaddingValues,  modifier: Modifier = Modifier){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        color = Color(red = 25, green = 27, blue = 26)
    ) {
        LazyColumn(){
            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    text = "FAQs",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            item {
                Divider(
                    modifier = modifier
                        .padding(start = 25.dp, end = 25.dp, bottom = 15.dp),
                    color = Color.White
                )
            }
            items(listaDePreguntas){
                TarjetaPregunta(pregunta = it)
            }
            item {
                Divider(
                    modifier = modifier
                        .padding(start = 25.dp, end = 25.dp, top = 15.dp, bottom = 15.dp),
                    color = Color.White
                )
            }



        }

    }

}

@Composable
fun TarjetaPregunta(
    modifier: Modifier = Modifier,
    pregunta: Pregunta,

) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if(expanded) Color(red = 66, green = 59, blue = 59) else Color(red = 25, green = 27, blue = 26),
        label = "",
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 25.dp, top = 15.dp, end = 25.dp, bottom = 15.dp)
    ) {

        Column(modifier = Modifier
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            .background(color = color)
        ) {
            Row(modifier = modifier.fillMaxWidth()){
                Icon(
                    modifier = modifier.padding(15.dp),
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
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White
                )

                BotonPregunta(
                    expanded = expanded,
                    onClick = { expanded = !expanded}
                )
            }
            if(expanded){
                Text(
                    modifier = modifier.padding(start = 20.dp, end = 20.dp),
                    text = stringResource(id = pregunta.respuesta),
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
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
        onClick = onClick,
        modifier = modifier
    ){
        Icon(
            imageVector = if(expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = "",
            tint = Color.White
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    TrovareTheme {
        FAQS()
    }
}