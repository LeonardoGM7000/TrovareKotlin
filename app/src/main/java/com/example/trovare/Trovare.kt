package com.example.trovare

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trovare.ui.theme.Pantallas.Configuracion
import com.example.trovare.ui.theme.Pantallas.FAQS
import com.example.trovare.ui.theme.Pantallas.Inicio
import com.example.trovare.ui.theme.Pantallas.Soporte
import androidx.compose.runtime.collectAsState
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Buscar
import com.example.trovare.ui.theme.Pantallas.Detalles
import com.example.trovare.ui.theme.Pantallas.Perfil
import com.google.android.libraries.places.api.net.PlacesClient

/**
 * enum values that represent the screens in the app
 */
enum class PantallasTrovare(val titulo: String) {
    Inicio(titulo = "Inicio"),
    Configuracion(titulo = "Configuracion"),
    FAQS(titulo = "FAQS"),
    Soporte(titulo = "Soporte"),
    Perfil(titulo = "Perfil"),
    Buscar(titulo = "Buscar"),
    Detalles(titulo = "Detalles"),

}


@Composable
fun Trovare(
    viewModel: TrovareViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    placesClient: PlacesClient
){
// Obtener la entrada actual en la pila de retroceso
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Obtener el nombre de la pantalla actual
    val currentScreen = PantallasTrovare.valueOf(
        backStackEntry?.destination?.route ?: PantallasTrovare.Inicio.name
    )

    val uiState by viewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = PantallasTrovare.Inicio.name,
        //enterTransition = {  slideInHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { 0 }  },
        //exitTransition = { slideOutHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { -300 }  }
        //enterTransition = { fadeIn() },
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}

    ) {
        composable(route = PantallasTrovare.Inicio.name){
            Inicio(
                navController = navController
            )
        }
        composable(route = PantallasTrovare.Configuracion.name){
            Configuracion(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = PantallasTrovare.FAQS.name){
            FAQS(
                navController = navController
            )
        }
        composable(route = PantallasTrovare.Soporte.name){
            Soporte(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = PantallasTrovare.Perfil.name){
            Perfil(
                navController = navController
            )
        }
        composable(route = PantallasTrovare.Buscar.name){
            Buscar(
                navController = navController,
                viewModel = viewModel,
                placesClient = placesClient
            )
        }
        composable(route = PantallasTrovare.Detalles.name){
            Detalles()
        }
    }
}