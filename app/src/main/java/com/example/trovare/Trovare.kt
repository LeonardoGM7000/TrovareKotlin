package com.example.trovare

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trovare.ui.theme.Pantallas.Configuracion
import com.example.trovare.ui.theme.Pantallas.FAQS
import com.example.trovare.ui.theme.Pantallas.Inicio
import com.example.trovare.ui.theme.Pantallas.Soporte
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Administrador
import com.example.trovare.ui.theme.Pantallas.Bienvenida
import com.example.trovare.ui.theme.Pantallas.Buscar
import com.example.trovare.ui.theme.Pantallas.CrearCuenta
import com.example.trovare.ui.theme.Pantallas.Detalles
import com.example.trovare.ui.theme.Pantallas.EditarPerfil
import com.example.trovare.ui.theme.Pantallas.EditarPreguntas
import com.example.trovare.ui.theme.Pantallas.EliminarComentarios
import com.example.trovare.ui.theme.Pantallas.EliminarCuentas
import com.example.trovare.ui.theme.Pantallas.InicioDeSesion
import com.example.trovare.ui.theme.Pantallas.PerfilConfiguracion
import com.example.trovare.ui.theme.Pantallas.PerfilInicio
import com.example.trovare.ui.theme.Pantallas.PreguntasAdmin
import com.google.android.libraries.places.api.net.PlacesClient


sealed class Pantalla(val ruta: String) {
    object Bienvenida : Pantalla("Bienvenida")
    object InicioDeSesion : Pantalla("InicioDeSesion")
    object Registro : Pantalla("Registro")
    object Inicio : Pantalla("Inicio")
    object Configuracion : Pantalla("Configuracion")
    object FAQS : Pantalla("FAQS")
    object Soporte : Pantalla("Soporte")
    object PerfilInicio : Pantalla("PerfilInicio")
    object PerfilConfiguracion : Pantalla("PerfilConfiguracion")
    object Buscar : Pantalla("Buscar")
    object Detalles : Pantalla("Detalles")
    object EditarPerfil : Pantalla("EditarPerfil")
    object Administrador : Pantalla("Administrador")
    object PreguntasAdmin : Pantalla("PreguntasAdmin")
    object EditarPreguntas : Pantalla("EditarPreguntas")
    object EliminarCuentas : Pantalla("EliminarCuentas")
    object EliminarComentarios : Pantalla("EliminarComentarios")

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
) {

    NavHost(
        navController = navController,
        startDestination = Pantalla.Bienvenida.ruta,
        //enterTransition = {  slideInHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { 0 }  },
        //exitTransition = { slideOutHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { -300 }  }
        //enterTransition = { fadeIn() },
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }

    ) {
        composable(route = Pantalla.Bienvenida.ruta) {
            Bienvenida(
                navController = navController
            )
        }
        composable(route = Pantalla.InicioDeSesion.ruta) {
            InicioDeSesion(
                navController = navController
            )
        }
        composable(route = Pantalla.Registro.ruta) {
            CrearCuenta(
                navController = navController
            )
        }
        composable(route = Pantalla.Inicio.ruta) {
            Inicio(
                navController = navController
            )
        }
        composable(route = Pantalla.Configuracion.ruta) {
            Configuracion(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = Pantalla.FAQS.ruta) {
            FAQS(
                navController = navController
            )
        }
        composable(route = Pantalla.Soporte.ruta) {
            Soporte(
                viewModel = viewModel,
                navController = navController
            )
        }
        composable(route = Pantalla.PerfilInicio.ruta) {
            PerfilInicio(
                navController = navController
            )
        }
        composable(route = Pantalla.PerfilConfiguracion.ruta) {
            PerfilConfiguracion(
                navController = navController
            )
        }
        composable(route = Pantalla.EditarPerfil.ruta) {
            EditarPerfil(
                navController = navController
            )
        }
        composable(route = Pantalla.Administrador.ruta) {
            Administrador(
                navController = navController
            )
        }
        composable(route = Pantalla.PreguntasAdmin.ruta) {
            PreguntasAdmin(
                navController = navController
            )
        }
        composable(route = Pantalla.EditarPreguntas.ruta + "/{preguntaId}") { navBackStackEntry ->
            val preguntaId = navBackStackEntry.arguments?.getString("preguntaId") ?: ""
            EditarPreguntas(
                navController = navController,
                preguntaId = preguntaId
            )
        }

        composable(route = Pantalla.EliminarCuentas.ruta) {
            EliminarCuentas(
                navController = navController
            )
        }
        composable(route = Pantalla.EliminarComentarios.ruta) {
            EliminarComentarios(
                navController = navController
            )
        }

        composable(route = Pantalla.Buscar.ruta) {
            Buscar(
                navController = navController,
                viewModel = viewModel,
                placesClient = placesClient
            )
        }
        composable(
            route = Pantalla.Detalles.ruta + "/{lugar}",
            arguments = listOf(
                navArgument("lugar") {
                    type = NavType.StringType
                    defaultValue = "Prueba"
                    nullable = true
                }
            )
        ) {
            Detalles(
                placeId = it.arguments?.getString("lugar"),
                placesClient = placesClient,
                viewModel = viewModel,
                navController = navController,
            )
        }
    }
}