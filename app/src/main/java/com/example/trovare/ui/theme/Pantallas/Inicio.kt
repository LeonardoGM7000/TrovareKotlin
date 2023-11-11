package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attractions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trovare.Pantalla
import com.example.trovare.R
import com.example.trovare.ui.theme.Data.listaDeExplorar
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.MenuInferior
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv5
import com.example.trovare.ui.theme.Trv9
import kotlin.math.absoluteValue


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Inicio(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val pagerState = rememberPagerState(initialPage = 1) { listaDeExplorar.size }

    Scaffold(
        topBar = {
            BarraSuperiorConfig(navController)
        },
        bottomBar = {

            MenuInferior(
                presionarHome = {},
                presionarPerfil = {
                    navController.navigate(Pantalla.PerfilInicio.ruta)
                },
                presionarNavegacion = {
                    navController.navigate(Pantalla.Mapa.ruta)
                },
                presionarItinerario = {},
                colorHome = Trv5,

            )

        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Barra-de-búsqueda-----------------------------------------------------------------
                Card(
                    modifier = modifier
                        .padding(horizontal = 50.dp)
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = NoRippleInteractionSource()
                        ) { navController.navigate(Pantalla.Buscar.ruta) },
                    colors = CardDefaults.cardColors(Color.Black),
                    border = CardDefaults.outlinedCardBorder()
                ){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = modifier.padding(15.dp),
                            imageVector = Icons.Rounded.TravelExplore,
                            contentDescription = "",
                            tint = Color.White
                        )
                        Text(
                            text = "Busca lugares de interés",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }


                }
                //Categorías------------------------------------------------------------------------



                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, top = 15.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Categorías",
                        style = MaterialTheme.typography.displaySmall
                    )
                    TarjetaCategorias()
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, bottom = 15.dp),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Explora más sitios",
                        style = MaterialTheme.typography.displaySmall
                    )
                }

                //Carrusel-explorar-mas-sitios------------------------------------------------------
                Row (
                    modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically

                ){
                    HorizontalPager(

                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 100.dp)
                    ) { pagina ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = modifier
                                    .graphicsLayer {
                                        val pageOffset = (
                                                (pagerState.currentPage - pagina) + pagerState
                                                    .currentPageOffsetFraction
                                                ).absoluteValue

                                        alpha = lerp(
                                            start = 0.5f,
                                            stop = 1f,
                                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                        )
                                            .also { scale ->
                                                scaleX = scale
                                                scaleY = scale
                                            }
                                    }
                            ){
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = modifier.fillMaxWidth(),
                                        text = listaDeExplorar[pagina].Titulo,
                                        textAlign = TextAlign.Center,
                                    )
                                    Card(
                                        modifier = modifier
                                            .padding(vertical = 10.dp)
                                            .fillMaxHeight(1f)
                                            .fillMaxWidth(1f)

                                    ) {
                                        Box() {
                                            AsyncImage(
                                                modifier = modifier.fillMaxSize(),
                                                model = listaDeExplorar[pagina].Imagen,
                                                contentDescription = null,
                                                contentScale = ContentScale.FillBounds,
                                                placeholder = painterResource(id = R.drawable.image_placeholder),
                                                error = painterResource(id = R.drawable.error)
                                            )
                                            Box(modifier = Modifier
                                                .fillMaxSize()
                                                .background(
                                                    brush = Brush.verticalGradient(
                                                        colors = listOf(
                                                            Color.Transparent,
                                                            Color.Black
                                                        ),
                                                        startY = 500f,
                                                    )
                                                )
                                            ){
                                                Column(
                                                    modifier = modifier.fillMaxSize(),
                                                    verticalArrangement = Arrangement.Bottom
                                                ) {
                                                    Text(
                                                        modifier = modifier.fillMaxWidth(),
                                                        text = ("${listaDeExplorar[pagina].lugar}"),
                                                        textAlign = TextAlign.Center,

                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


//Tarjeta para mostrar categorías-------------------------------------------------------------------
@Composable
fun TarjetaCategorias(modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Trv2
        ),
    ){
        Row (
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Attractions, nombre = "Atracciones")
                Categoria(icono = Icons.Filled.Restaurant, nombre = "Restaurante")
            }
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Museum, nombre = "Museos")
                Categoria(icono = Icons.Filled.Park, nombre = "Parques")
            }
            Column(
                modifier = modifier.
                    padding(top = 15.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Categoria(icono = Icons.Filled.Hotel, nombre = "Hoteles")
                Categoria(icono = Icons.Filled.Favorite, nombre = "Favoritos")
            }
        }


    }
}
//Categoria para ser mostrada en la tarjeta de categorías-------------------------------------------
@Composable
fun Categoria(icono: ImageVector, nombre: String,  modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .size(width = 105.dp, height = 85.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                containerColor = Color.White
            ){
                Icon(
                    imageVector = icono,
                    contentDescription = ""
                )
            }
            Text(
                text = nombre,
                textAlign = TextAlign.Center,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
}





