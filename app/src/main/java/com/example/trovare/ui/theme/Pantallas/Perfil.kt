package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(
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
            PerfilPrincipal()

        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PerfilPrincipal(
    modifier: Modifier = Modifier,
    usuario: Usuario = usuarioPrueba
){
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
                    text = "Perfil",
                    style = MaterialTheme.typography.displayMedium
                )
            }
            item {
                Spacer(modifier = modifier.height(10.dp))
            }
            item {
                //Tarjeta con la informacion del usuario--------------------------------------------
                Card(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Trv1
                    ),
                    border = CardDefaults.outlinedCardBorder(),
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
                                painter = painterResource(id = usuario.foto_perfil),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        Column(
                            modifier = modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                modifier = modifier
                                    .padding(top = 5.dp)
                                    .fillMaxHeight(),
                                text = usuario.nombre,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,

                                )
                            Text(
                                text = "Se unió en ${usuario.fechaDeRegistro}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Icon(
                                modifier = modifier
                                    .clickable { /* POR HACER */ }
                                    .padding(vertical = 5.dp),
                                imageVector = Icons.Rounded.Edit,
                                contentDescription = "",
                                tint = Color.White
                            )
                                

                        }
                    }
                }
            }
            //Descripcion del usuario---------------------------------------------------------------
            item {
                if(usuario.descripcion != null){
                    Text(
                        modifier = modifier
                            .padding(horizontal = 25.dp, vertical = 10.dp)
                            .fillMaxWidth(),
                        text = usuario.descripcion ?: "",
                        textAlign = TextAlign.Justify,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {

                }
            }
            item {
                if (usuario.lugarDeOrigen != null){
                    Row (
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            modifier = modifier.padding(end = 5.dp),
                            imageVector = Icons.Rounded.LocationOn,
                            contentDescription = ""
                        )
                        Text(
                            text = "Lugar de origen: ${usuario.lugarDeOrigen}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                } else {

                }
            }
            item {
                Divisor()
            }
        }

    }
}