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
import com.example.trovare.Api.apiBuscarPorCategorias
import com.example.trovare.Api.apiBuscarPorPopularidad
import com.example.trovare.Api.apiBuscarPuntosDeInteres
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Navegacion.Trovare
import com.example.trovare.ui.theme.TrovareTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    // Creamos las llaves para sharedPreferences
    private val KEY_PREFERENCES = "Configuracion"
    private val KEY_IDIOMA = "idioma"
    private val KEY_UNIDADES = "unidades"
    private val KEY_MONEDA = "moneda"

    //Solicitar permisos de ubicación---------------------------------------------------------------

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

    //Shared preferences----------------------------------------------------------------------------
    private val KEY_EMAIL = "correo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*colocar aqui API de google places AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc*/
        Places.initialize(this, /*colocar aqui llave de API de google places*/"AIzaSyDiFpHGDFegDBzku5qKvnGniIN88T6vuQc")//Inicializar API de Places
        val placesClient: PlacesClient = Places.createClient(this)//Crear cliente
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        //getLastLocation()
        viewModel.getLastLocation(fusedLocationProviderClient = fusedLocationProviderClient)

        // Verificamos si el usuario ya inicio sesión previamente

        // Creamos una instancia de firebase para verificar el usuario registrado
        val auth = FirebaseAuth.getInstance()

        //Recuperar ubicacion actual de usuario

        //alIniciar---------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------
        val ubicacionActual = viewModel.ubicacionActual.value

        apiBuscarPorCategorias(
            filtro = "Restaurantes",
            ubicacion = ubicacionActual,
            viewModel = viewModel,
            placesClient = placesClient
        )

        apiBuscarPorPopularidad(
            ubicacion = ubicacionActual,
            viewModel = viewModel,
            placesClient = placesClient,
        )

        apiBuscarPuntosDeInteres(
            ubicacion = ubicacionActual,
            viewModel = viewModel,
            placesClient = placesClient
        )

        // Variable que almacena la ruta de la pantalla
        var ruta = "Bienvenida"

        try{
            Log.d("Main_Trovare", auth.currentUser?.email.toString())

            if(auth.currentUser != null){
                ruta = Pantalla.NavegacionSecundaria.ruta

            }else{
                auth.signOut()
                ruta = Pantalla.Bienvenida.ruta
            }


        }catch(e:Exception){
            Log.d("Main_Trovare", "Error en la conexión de la base de datos")
        }

        viewModel.obtenerDato()

        // SharedPreferences -> Configuración
        val sharedPreferences = getSharedPreferences(KEY_PREFERENCES, MODE_PRIVATE)

        viewModel.setIdioma(sharedPreferences.getString(KEY_IDIOMA,"Español").toString())
        viewModel.setUnidades(sharedPreferences.getString(KEY_UNIDADES, "Km/m").toString())

        setContent {
            TrovareTheme {
                Trovare(
                    placesClient = placesClient,
                    viewModel = viewModel,
                    pantallaInicial = ruta,
                    fusedLocationProviderClient = fusedLocationProviderClient,
                    context = this
                )
                //MapScreen(state = viewModel.state.value, viewModel = viewModel, fusedLocationProviderClient = fusedLocationProviderClient)
            }
        }
    }
}





