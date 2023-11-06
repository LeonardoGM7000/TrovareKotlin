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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.trovare.ui.theme.Data.Lugar
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Buscar
import com.example.trovare.ui.theme.Pantallas.Detalles
import com.example.trovare.ui.theme.Pantallas.Perfil
import com.google.android.libraries.places.api.net.PlacesClient


sealed class Pantalla(val ruta: String){
    object Inicio: Pantalla("Inicio")
    object Configuracion: Pantalla("Configuracion")
    object FAQS: Pantalla("FAQS")
    object Soporte: Pantalla("Soporte")
    object Perfil: Pantalla("Perfil")
    object Buscar: Pantalla("Buscar")
    object Detalles: Pantalla("Detalles")

    fun conArgs(vararg args: String): String {
        return buildString {
            append(ruta)
            args.forEach { arg ->
                append("/$arg")
            }
        }

    }

}


@Composable
fun Trovare(
    viewModel: TrovareViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
    placesClient: PlacesClient
){

    NavHost(
        navController = navController,
        startDestination = Pantalla.Inicio.ruta,
        //enterTransition = {  slideInHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { 0 }  },
        //exitTransition = { slideOutHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { -300 }  }
        //enterTransition = { fadeIn() },
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}

    ) {
        composable(route = Pantalla.Inicio.ruta){
            Inicio(
                navController = navController
            )
        }
        composable(route = Pantalla.Configuracion.ruta){
            Configuracion(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = Pantalla.FAQS.ruta){
            FAQS(
                navController = navController
            )
        }
        composable(route = Pantalla.Soporte.ruta){
            Soporte(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = Pantalla.Perfil.ruta){
            Perfil(
                navController = navController
            )
        }
        composable(route = Pantalla.Buscar.ruta){
            Buscar(
                navController = navController,
                viewModel = viewModel,
                placesClient = placesClient
            )
        }
        composable(
            route = Pantalla.Detalles.ruta + "/{lugar}",
            arguments = listOf(
                navArgument("lugar"){
                    type = NavType.StringType
                    defaultValue = "Prueba"
                    nullable = true
                }
            )
        ){
            Detalles(
                placeId = it.arguments?.getString("lugar"),
                navController = navController,
            )
        }
    }
}