package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Pantallas.Perfil.PerfilPrincipal
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.Divisor2
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2

@Composable
fun Favoritos(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: TrovareViewModel
){
    Scaffold(
        topBar = {
            BarraSuperiorConfig(navController = navController)
        },
    ) { it ->
        
        val usuario by viewModel.usuario.collectAsState()
        
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            LazyColumn(
                modifier = modifier.padding(horizontal = 25.dp)
            ){
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Favoritos",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor2()
                }
                if(usuario.favoritos.isEmpty()){
                    item {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                modifier = modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center),
                                text = "No hay lugares favoritos",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }

                    }
                } else {
                    items(usuario.favoritos){lugarFavorito ->
                        Card(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Pantalla.Detalles.conArgs(lugarFavorito.id))
                                },
                            colors = CardDefaults.cardColors(Trv1),
                            border = CardDefaults.outlinedCardBorder()
                        ){
                            Text(
                                modifier = modifier
                                    .padding(3.dp),
                                text = lugarFavorito.nombre,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                }
            }
        }
    }
}