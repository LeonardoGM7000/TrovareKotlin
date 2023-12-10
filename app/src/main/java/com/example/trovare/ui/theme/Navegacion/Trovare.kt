package com.example.trovare.ui.theme.Navegacion

import android.content.Context
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
import com.example.trovare.ui.theme.Pantallas.Soporte
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.trovare.ui.theme.Pantallas.Ingreso.Bienvenida
import com.example.trovare.ui.theme.Pantallas.Buscar
import com.example.trovare.ui.theme.Pantallas.Detalles
import com.example.trovare.ui.theme.Pantallas.Perfil.EditarPerfil
import com.example.trovare.ui.theme.Pantallas.Ingreso.InicioDeSesion
import com.example.trovare.ui.theme.Pantallas.Itinerarios.EditarItinerario
import com.example.trovare.ui.theme.Pantallas.Perfil.PerfilConfiguracion
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Pantallas.Administrador
import com.example.trovare.ui.theme.Pantallas.CrearCuenta
import com.example.trovare.ui.theme.Pantallas.EditarPreguntas
import com.example.trovare.ui.theme.Pantallas.EliminarComentarios
import com.example.trovare.ui.theme.Pantallas.EliminarCuentas
import com.example.trovare.ui.theme.Pantallas.Ingreso.ActualizarContrasena
import com.example.trovare.ui.theme.Pantallas.Ingreso.RecuperarContrasena
import com.example.trovare.ui.theme.Pantallas.Ingreso.TokenRecuperarContrasena
import com.example.trovare.ui.theme.Pantallas.Itinerarios.AgregarLugarItinerario
import com.example.trovare.ui.theme.Pantallas.Itinerarios.RutasItinerario
import com.example.trovare.ui.theme.Pantallas.PreguntasAdmin
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient


sealed class Pantalla(val ruta: String) {
    object Bienvenida : Pantalla("Bienvenida")
    object InicioDeSesion : Pantalla("InicioDeSesion")
    object Registro : Pantalla("Registro")
    object NavegacionSecundaria : Pantalla("NavegacionSecundaria")
    object Inicio : Pantalla("Inicio")
    object Configuracion : Pantalla("Configuracion")
    object FAQS : Pantalla("FAQS")
    object Soporte : Pantalla("Soporte")
    object PerfilInicio : Pantalla("PerfilInicio")
    object PerfilConfiguracion : Pantalla("PerfilConfiguracion")
    object Favoritos : Pantalla("Favoritos")
    object Buscar : Pantalla("Buscar")
    object Detalles : Pantalla("Detalles")
    object EditarPerfil : Pantalla("EditarPerfil")
    object Mapa : Pantalla("Mapa")
    object Itinerarios : Pantalla("Itinerarios")
    object EditarItinerario : Pantalla("EditarItinerario")
    object AgregarLugarItinerario : Pantalla("AgregarLugarItinerario")
    object AgregarRutaALugar : Pantalla("AgregarRutaALugar")
    object Administrador : Pantalla("Administrador")
    object PreguntasAdmin : Pantalla("PreguntasAdmin")
    object EditarPreguntas : Pantalla("EditarPreguntas")
    object EliminarCuentas : Pantalla("EliminarCuentas")
    object EliminarComentarios : Pantalla("EliminarComentarios")
    object RecuperarContrasena : Pantalla("RecuperarContrasena")
    object TokenRecuperarContrasena : Pantalla("TokenRecuperarContrasena")
    object ActualizarContrasena : Pantalla("ActualizarContrasena")


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
    pantallaInicial: String,
    placesClient: PlacesClient,
    fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
){

    NavHost(
        navController = navController,
        startDestination = pantallaInicial,
        //enterTransition = {  slideInHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { 0 }  },
        //exitTransition = { slideOutHorizontally(animationSpec = SpringSpec(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium,)) { -300 }  }
        //enterTransition = { fadeIn() },
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None}

    ) {
        //Bienvendia--------------------------------------------------------------------------------
        composable(route = Pantalla.Bienvenida.ruta) {
            Bienvenida(
                navController = navController
            )
        }
        //Inicio de sesión--------------------------------------------------------------------------
        composable(route = Pantalla.InicioDeSesion.ruta) {
            InicioDeSesion(
                navController = navController,
                viewModel = viewModel
            )
        }
        //Crear cuenta------------------------------------------------------------------------------
        composable(route = Pantalla.Registro.ruta) {
            CrearCuenta(
                navController = navController
            )
        }
        //Navegación Secundaria---------------------------------------------------------------------
        composable(route = Pantalla.NavegacionSecundaria.ruta) {
            NavegacionSecundaria(
                viewModel = viewModel,
                navController = navController,
                placesClient = placesClient,
                fusedLocationProviderClient = fusedLocationProviderClient
            )
        }
        //Configuración----------------------------------------------------------------------------
        composable(route = Pantalla.Configuracion.ruta) {
            Configuracion(
                viewModel = viewModel,
                navController = navController,
            )
        }
        //FAQS--------------------------------------------------------------------------------------
        composable(route = Pantalla.FAQS.ruta) {
            FAQS(
                navController = navController
            )
        }
        //Soporte-----------------------------------------------------------------------------------
        composable(route = Pantalla.Soporte.ruta) {
            Soporte(
                viewModel = viewModel,
                navController = navController
            )
        }
        //PerfilConfiguración-----------------------------------------------------------------------
        composable(route = Pantalla.PerfilConfiguracion.ruta) {
            PerfilConfiguracion(
                navController = navController,
                viewModel = viewModel
            )
        }
        //EditarPerfil------------------------------------------------------------------------------
        composable(route = Pantalla.EditarPerfil.ruta) {
            EditarPerfil(
                navController = navController,
                viewModel = viewModel
            )
        }
        //BuscarLugares-----------------------------------------------------------------------------
        composable(route = Pantalla.Buscar.ruta) {
            Buscar(
                navController = navController,
            )
        }
        //DetallesDelLugar--------------------------------------------------------------------------
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
        //EditarItinerario--------------------------------------------------------------------------
        composable(route = Pantalla.EditarItinerario.ruta) {
            EditarItinerario(
                navController = navController,
                viewModel = viewModel
            )
        }
        //AgregarLugarAItinerario-------------------------------------------------------------------
        composable(route = Pantalla.AgregarLugarItinerario.ruta) {
            AgregarLugarItinerario(
                navController = navController,
                state = viewModel.state.value,
                viewModel = viewModel,
                fusedLocationProviderClient = fusedLocationProviderClient,
                placesClient = placesClient
            )
        }
        //AgregarRutaALugar-------------------------------------------------------------------------
        composable(route = Pantalla.AgregarRutaALugar.ruta) {
            RutasItinerario(
                state = viewModel.state.value,
                navController = navController,
                viewModel = viewModel,
                fusedLocationProviderClient = fusedLocationProviderClient,
                placesClient = placesClient
            )
        }
        //Administrador-----------------------------------------------------------------------------
        composable(route = Pantalla.Administrador.ruta) {
            Administrador(
                navController = navController
            )
        }
        //PreguntasAdmin----------------------------------------------------------------------------
        composable(route = Pantalla.PreguntasAdmin.ruta) {
            PreguntasAdmin(
                navController = navController
            )
        }
        //EditarPreguntas---------------------------------------------------------------------------
        composable(route = Pantalla.EditarPreguntas.ruta + "/{preguntaId}") { navBackStackEntry ->
            val preguntaId = navBackStackEntry.arguments?.getString("preguntaId") ?: ""
            EditarPreguntas(
                navController = navController,
                preguntaId = preguntaId
            )
        }
        //EliminarCuentas---------------------------------------------------------------------------
        composable(route = Pantalla.EliminarCuentas.ruta) {
            EliminarCuentas(
                navController = navController
            )
        }
        //EliminarComentarios-----------------------------------------------------------------------
        composable(route = Pantalla.EliminarComentarios.ruta) {
            EliminarComentarios(
                navController = navController
            )
        }
        //RecuperarContrasena-----------------------------------------------------------------------
        composable(route = Pantalla.RecuperarContrasena.ruta) {
            RecuperarContrasena(
                navController = navController
            )
        }
        //TokenParaRecuperarContrasen---------------------------------------------------------------
        composable(route = Pantalla.TokenRecuperarContrasena.ruta) {
            TokenRecuperarContrasena(
                navController = navController
            )
        }
        //ActualizarContrasena----------------------------------------------------------------------
        composable(route = Pantalla.ActualizarContrasena.ruta) {
            ActualizarContrasena(
                navController = navController
            )
        }
    }

}
