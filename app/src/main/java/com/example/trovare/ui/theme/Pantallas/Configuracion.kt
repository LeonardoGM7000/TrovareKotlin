package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trovare.R
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.TrovareTheme
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configuracion() {

    Scaffold(
        topBar = {
            BarraSuperior()
        },
    ) { it ->
        CuerpoConfiguracion(padding = it)
    }
}

@Composable
fun CuerpoConfiguracion(padding: PaddingValues, modifier: Modifier = Modifier){
    Surface(
        modifier = modifier
            .fillMaxSize()
            .padding(padding),
        color = Trv1
    ){
        LazyColumn(){
            item {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    text = "Configuración",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            item{
                TarjetaPerfil()
                }
            item {
               Divisor()
            }
        }
    }

}

@Composable
fun TarjetaPerfil(imagen: Painter = painterResource(id = R.drawable.perfil), nombre: String = "Nombre usuario", modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Trv2
        ),
    ) {
        Row(modifier = modifier
            .fillMaxWidth()
        ){
            Card(
                modifier = modifier
                    .padding(10.dp)
                    .size(80.dp),
                shape = RoundedCornerShape(100.dp)
            ) {
                Image(
                    modifier = modifier.fillMaxSize(),
                    painter = imagen,
                    contentDescription = "",
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    modifier = modifier.padding(vertical =  5.dp),
                    text = nombre,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    modifier = modifier
                        .clickable {  },
                    text = "Ver Perfil",
                    style = MaterialTheme.typography.bodySmall,
                    textDecoration = TextDecoration.Underline,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewConfiguracion() {
    TrovareTheme {
        Configuracion()
    }
}