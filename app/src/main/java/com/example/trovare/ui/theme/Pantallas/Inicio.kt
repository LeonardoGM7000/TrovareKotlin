package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trovare.ui.theme.Recursos.BarraSuperiorInicio
import com.example.trovare.ui.theme.Recursos.CircularButton
import com.example.trovare.ui.theme.TrovareTheme
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Inicio() {
    Scaffold(
        topBar = {
            BarraSuperiorInicio()
        },
    ) { it ->
        CuerpoSoporte(padding = it)
    }
}

@Composable
fun CuerpoSoporte(padding: PaddingValues, modifier: Modifier = Modifier){
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        color = Trv1
    ){
        Column {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                text = "Categorías",
                style = MaterialTheme.typography.displaySmall
            )
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Trv2
                ),
            ){
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircularButton()
                    CircularButton()
                    CircularButton()
                }

            }
        }

    }
}

@Preview
@Composable
fun preview(){
    TrovareTheme {
        Inicio()
    }
}