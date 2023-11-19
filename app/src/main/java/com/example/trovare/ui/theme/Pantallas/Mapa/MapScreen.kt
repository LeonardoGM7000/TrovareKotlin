package com.example.trovare.ui.theme.Pantallas.Mapa

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.trovare.ViewModel.TrovareViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.launch

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun MapScreen(
    state: MapState,
    viewModel: TrovareViewModel,
    fusedLocationProviderClient: FusedLocationProviderClient
    //setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    //calculateZoneViewCenter: () -> LatLngBounds,
) {

    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = state.lastKnownLocation != null,
    )
    val marcadorInicializado by remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val ubicacion by viewModel.ubicacion.collectAsState()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {

            //Si cambia la ubicacion,
            MapEffect(ubicacion) {
                viewModel.getLastLocation(fusedLocationProviderClient)
                val cameraPosition = CameraPosition.fromLatLngZoom(ubicacion, 15f)
                cameraPositionState.animate(CameraUpdateFactory.newCameraPosition(cameraPosition), 800)
            }

            // NOTE: Some features of the MarkerInfoWindow don't work currently. See docs:
            // https://github.com/googlemaps/android-maps-compose#obtaining-access-to-the-raw-googlemap-experimental
            // So you can use clusters as an alternative to markers.
            if(marcadorInicializado){

                MarkerInfoWindow(
                    state = rememberMarkerState(position = LatLng(49.1, -122.5)),
                    snippet = "Some stuff",
                    onClick = {
                        // This won't work :(
                        System.out.println("Mitchs_: Cannot be clicked")
                        true
                    },
                    draggable = true
                )
            }

        }
    }
//    // Center camera to include all the Zones.
//    LaunchedEffect(state.clusterItems) {
//        if (state.clusterItems.isNotEmpty()) {
//            cameraPositionState.animate(
//                update = CameraUpdateFactory.newLatLngBounds(
//                    calculateZoneViewCenter(),
//                    0
//                ),
//            )
//        }
//    }
}

/**
 * If you want to center on a specific location.
 */
private suspend fun CameraPositionState.centerOnLocation(
    location: Location
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        LatLng(location.latitude, location.longitude),
        15f
    ),
)