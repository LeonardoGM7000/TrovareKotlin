package com.example.trovare

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Trovare
import com.example.trovare.ui.theme.Pantallas.Itinerarios.EditarItinerario
import com.example.trovare.ui.theme.Pantallas.Mapa.MapScreen
import com.example.trovare.ui.theme.TrovareTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                viewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            viewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel: TrovareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*colocar aqui API de google places*/
        Places.initialize(this, /*colocar aqui llave de API de google places*/"AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ")//Inicializar API de Places
        val placesClient: PlacesClient = Places.createClient(this)//Crear cliente
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        //getLastLocation()
        viewModel.getLastLocation(fusedLocationProviderClient = fusedLocationProviderClient)
        setContent {
            TrovareTheme {
                Trovare(placesClient = placesClient, viewModel = viewModel, fusedLocationProviderClient = fusedLocationProviderClient)
                //MapScreen(state = viewModel.state.value, viewModel = viewModel, fusedLocationProviderClient = fusedLocationProviderClient)
        }
    }
}
}




