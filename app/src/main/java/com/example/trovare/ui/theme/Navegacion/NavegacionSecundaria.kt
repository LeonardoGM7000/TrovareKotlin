package com.example.trovare.ui.theme.Navegacion

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Inicio
import com.example.trovare.ui.theme.Pantallas.Itinerarios.Itinerarios
import com.example.trovare.ui.theme.Pantallas.Mapa.MapaPrincipal
import com.example.trovare.ui.theme.Pantallas.Perfil.PerfilInicio
import com.example.trovare.ui.theme.Recursos.BarraInferior
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient

data class NavegacionSecundaria2(
    val ruta: String,
    val iconoSeleccionado: ImageVector,
)

val DestinosNavegacionSecundaria = listOf(
    NavegacionSecundaria2(
        ruta = Pantalla.Inicio.ruta,
        iconoSeleccionado = Icons.Rounded.Home,
    ),
    NavegacionSecundaria2(
        ruta = Pantalla.PerfilInicio.ruta,
        iconoSeleccionado = Icons.Rounded.Person
    ),
    NavegacionSecundaria2(
        ruta = Pantalla.Mapa.ruta,
        iconoSeleccionado = Icons.Rounded.Map
    ),
    NavegacionSecundaria2(
        ruta = Pantalla.Itinerarios.ruta,
        iconoSeleccionado = Icons.Rounded.CalendarToday
    )
)

//Verificar la ruta actual del navHost
@Composable
fun rutaActual(navController: NavHostController): String? =
    navController.currentBackStackEntryAsState().value?.destination?.route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavegacionSecundaria(
    modifier: Modifier = Modifier,
    viewModel: TrovareViewModel,
    navController: NavHostController,
    navControllerSecundario: NavHostController = rememberNavController(),
    placesClient: PlacesClient,
    fusedLocationProviderClient: FusedLocationProviderClient
){

    Scaffold(
        bottomBar = {
            BarraInferior(
                navControllerSecundaario = navControllerSecundario
            )
        }
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navControllerSecundario,
            startDestination = Pantalla.Inicio.ruta,
            enterTransition = { EnterTransition.None},
            exitTransition = { ExitTransition.None}

        ) {
            composable(route = Pantalla.Inicio.ruta){
                Inicio(
                    navController = navController,
                    viewModel = viewModel,
                    fusedLocationProviderClient = fusedLocationProviderClient,
                )
            }
            composable(route = Pantalla.PerfilInicio.ruta){
                PerfilInicio(
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(route = Pantalla.Mapa.ruta){
                MapaPrincipal(
                    state = viewModel.state.value,
                    navController = navController,
                    viewModel = viewModel,
                    placesClient = placesClient,
                    fusedLocationProviderClient = fusedLocationProviderClient
                )
            }
            composable(route = Pantalla.Itinerarios.ruta){
                Itinerarios(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}





