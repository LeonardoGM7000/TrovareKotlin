package com.example.trovare.ui.theme.Pantallas.Mapa

/*

import androidx.compose.foundation.layout.Box
import com.google.maps.android.compose.GoogleMap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.BarraSuperiorConfig
import com.example.trovare.ui.theme.Recursos.MenuInferior
import com.example.trovare.ui.theme.Trv5
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaPrincipal(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 10f)
    }


    Scaffold(
        topBar = {
            BarraSuperior(navController)
        },
        bottomBar = {
            MenuInferior(
                presionarHome = {navController.navigate(Pantalla.Inicio.ruta)},
                presionarPerfil = {navController.navigate(Pantalla.PerfilInicio.ruta)},
                presionarNavegacion = {},
                presionarItinerario = {},
                colorHome = Trv5
            )

        },
    ){
        Box {
            GoogleMap(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = MarkerState(position = singapore),
                    title = "Singapore",
                    snippet = "Marker in Singapore"
                )
            }

        }


    }



}

 */



