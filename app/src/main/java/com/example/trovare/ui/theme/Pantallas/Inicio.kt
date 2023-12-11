package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Attractions
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.trovare.Api.apiBuscarPorCategorias
import com.example.trovare.Data.Categoria
import com.example.trovare.Data.NearbyPlaces
import com.example.trovare.Data.categorias
import com.example.trovare.Data.lugarTemporal
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv10
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv7
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient
import kotlin.math.absoluteValue

// Prueba
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Inicio(
    modifier: Modifier = Modifier,
    viewModel: TrovareViewModel,
    navController: NavController,
    fusedLocationProviderClient: FusedLocationProviderClient,
    placesClient: PlacesClient
) {
    val estadoInicio by viewModel.estadoInicial.collectAsState()

    val pagerStatePopulares = rememberPagerState(initialPage = 0) { estadoInicio.lugaresPopulares.size }
    val pagerStatePuntuados = rememberPagerState(initialPage = 0) { estadoInicio.lugaresPuntosDeInteres.size }
    val ubicacionActual by viewModel.ubicacionActual.collectAsState()

    Scaffold(
        topBar = {
            BarraSuperiorConfig(navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ){
            LazyColumn(
                modifier = modifier
                    .padding(horizontal = 25.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                //Barra de búsqueda-----------------------------------------------------------------
                item{
                    Card(
                        modifier = modifier
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
                }
                item { 
                    Spacer(modifier = modifier.height(10.dp))
                }
                //Categorías------------------------------------------------------------------------
                //----------------------------------------------------------------------------------

                //seleccionar cateogrías------------------------------------------------------------

                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Left,
                            color = Color.White,
                            text = "Categorías",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                item {
                    LazyRow(){
                        items(categorias){categoria ->
                            FilterChip(
                                modifier = modifier.padding(horizontal = 5.dp),
                                selected = (estadoInicio.categoriaSeleccionada == categoria.nombre),
                                leadingIcon = {
                                    Icon(
                                        imageVector = categoria.icono,
                                        contentDescription = "",
                                    )
                                },
                                onClick = {
                                    viewModel.setCategoriaSeleccionada(categoria.nombre)
                                    viewModel.setLugaresCercanosInicializado(false)
                                    apiBuscarPorCategorias(
                                        filtro = categoria.nombre,
                                        ubicacion = ubicacionActual,
                                        viewModel = viewModel,
                                        placesClient = placesClient
                                    )
                                },
                                label = {
                                    Text(
                                        text = categoria.nombre,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Trv2,
                                    selectedLabelColor = Trv10,
                                    iconColor = Color.White,
                                    selectedLeadingIconColor = Trv10,
                                )
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                //Mostrar lugares por categorías----------------------------------------------------

                item {
                    Box(
                        modifier = modifier
                            .height(250.dp)
                            .fillMaxWidth()
                    ){
                        if(estadoInicio.lugaresCercanosInicializado){
                            LazyRow{
                                items(estadoInicio.lugaresCercanos){ lugar ->
                                    Log.d("testNoche","${lugar!!.imagen}")
                                    TarjetaLugar(lugar = lugar!!, navController = navController)
                                }
                            }
                        }
                        else {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }
                    }
                }

                item {

                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                //Lugares populaes------------------------------------------------------------------
                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Left,
                            color = Color.White,
                            text = "Populares",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                item {
                    Box(
                        modifier = modifier
                            .height(130.dp)
                            .fillMaxWidth()
                    ) {
                        if(estadoInicio.lugaresPopularesInicializado){
                            HorizontalPager(
                                state = pagerStatePopulares,
                                //contentPadding = PaddingValues(horizontal = 10.dp)
                            ) { pagina ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = modifier
                                            .graphicsLayer {
                                                val pageOffset = (
                                                        (pagerStatePopulares.currentPage - pagina) + pagerStatePopulares
                                                            .currentPageOffsetFraction
                                                        ).absoluteValue

                                                alpha = lerp(
                                                    start = 0.9f,
                                                    stop = 1f,
                                                    fraction = 1f - pageOffset.coerceIn(0.0f, 1f)
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
                                            TarjetaLugarExtendida(lugar = estadoInicio.lugaresPopulares[pagina]?:lugarTemporal, navController = navController)
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }

                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }
                //mostrar el estado actual del pager state------------------------------------------
                item {
                    Card(
                        modifier = modifier.wrapContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = Trv2
                        )
                    ){
                        Row {
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePopulares.currentPage == 0) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePopulares.currentPage == 1) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePopulares.currentPage == 2) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePopulares.currentPage == 3) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePopulares.currentPage == 4) Trv10 else Trv3
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }
                //Puntos de Interés--------------------------------------------------------------------

                item {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            modifier = modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Left,
                            color = Color.White,
                            text = "Cercanos a ti",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                item {
                    Box(
                        modifier = modifier
                            .height(130.dp)
                            .fillMaxWidth()
                    ) {
                        if(estadoInicio.lugaresPuntosDeInteresInicializado){
                            HorizontalPager(
                                state = pagerStatePuntuados,
                                //contentPadding = PaddingValues(horizontal = 10.dp)
                            ) { pagina ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = modifier
                                            .graphicsLayer {
                                                val pageOffset = (
                                                        (pagerStatePuntuados.currentPage - pagina) + pagerStatePuntuados
                                                            .currentPageOffsetFraction
                                                        ).absoluteValue

                                                alpha = lerp(
                                                    start = 0.9f,
                                                    stop = 1f,
                                                    fraction = 1f - pageOffset.coerceIn(0.0f, 1f)
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
                                            TarjetaLugarExtendida(lugar = estadoInicio.lugaresPuntosDeInteres[pagina]?:lugarTemporal, navController = navController)
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            CircularProgressIndicator(
                                modifier = modifier.align(Alignment.Center),
                                color = Color.White
                            )
                        }

                    }
                }

                item {
                    Spacer(modifier = modifier.height(5.dp))
                }

                item {
                    Card(
                        modifier = modifier.wrapContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = Trv2
                        )
                    ){
                        Row {
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePuntuados.currentPage == 0) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePuntuados.currentPage == 1) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePuntuados.currentPage == 2) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePuntuados.currentPage == 3) Trv10 else Trv3
                            )
                            Icon(
                                modifier = modifier
                                    .size(10.dp)
                                    .padding(3.dp),
                                imageVector = Icons.Rounded.Circle,
                                contentDescription = "",
                                tint = if(pagerStatePuntuados.currentPage == 4) Trv10 else Trv3
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun TarjetaLugar(
    modifier: Modifier = Modifier,
    lugar: NearbyPlaces,
    navController: NavController
){
    Card(
        modifier = modifier
            .size(width = 165.dp, height = 245.dp)
            .clickable {
                navController.navigate(Pantalla.Detalles.conArgs(lugar.id))
            },
        colors = CardDefaults.cardColors(
            containerColor = Trv2,
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = modifier.padding(5.dp)
        ) {
            //imagen del lugar----------------------------------------------------------------------
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ){
                if (lugar.imagen != null) {
                    Image(
                        bitmap = lugar.imagen!!,
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                } else {
                    Image(
                        modifier = modifier
                            .fillMaxSize(),
                        painter = painterResource(id = R.drawable.image_placeholder),
                        contentDescription = ""
                    )
                }
            }
            Text(
                modifier = modifier.padding(vertical = 5.dp),
                text = lugar.displayName?.text?:"",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2
            )
            //Tipo de lugar y calificacion del lugar------------------------------------------------
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ){
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Attractions,
                        contentDescription = ""
                    )
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Trv7)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = "",
                                tint = Trv10
                            )
                            Text(
                                modifier = modifier.padding(horizontal = 5.dp),
                                text = if(lugar.rating == null) "---" else lugar.rating.toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Trv10
                            )
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = modifier.padding(horizontal = 5.dp))
}

@Composable
fun TarjetaLugarExtendida(
    modifier: Modifier = Modifier,
    lugar: NearbyPlaces,
    navController: NavController
){
    Row(
        modifier = modifier
            .clickable(
                indication = null,
                interactionSource = NoRippleInteractionSource()
            ) { navController.navigate(Pantalla.Detalles.conArgs(lugar.id)) },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Card(
            modifier = modifier
                .size(120.dp)
                .aspectRatio(1f)
        ){
            if (lugar.imagen != null) {
                Image(
                    bitmap = lugar.imagen!!,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )
            } else {
                Image(
                    modifier = modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.image_placeholder),
                    contentDescription = ""
                )
            }
        }

        Column(
            modifier = modifier.padding(start = 5.dp)
        ) {
            //Nombre del lugar----------------------------------------------------------------------
            Text(
                text = lugar.displayName?.text?:"",
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 2
            )
            //Icono y calificación del lugar--------------------------------------------------------
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Rounded.Attractions,
                    contentDescription = ""
                )
                Card(
                    colors = CardDefaults.cardColors(containerColor = Trv7)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Star,
                            contentDescription = "",
                            tint = Trv10
                        )
                        Text(
                            modifier = modifier.padding(horizontal = 5.dp),
                            text = if(lugar.rating == null) "---" else lugar.rating.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Trv10
                        )
                    }
                }
            }
        }
    }
}