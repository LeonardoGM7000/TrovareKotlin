package com.example.trovare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.trovare.ui.theme.Navegacion.Trovare
import com.example.trovare.ui.theme.Pantallas.Itinerarios.EditarItinerario
import com.example.trovare.ui.theme.TrovareTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*colocar aqui API de google places*/
        Places.initialize(this, /*colocar aqui llave de API de google places*/"AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ")//Inicializar API de Places
        val placesClient: PlacesClient = Places.createClient(this)//Crear cliente

        setContent {
            TrovareTheme {
                Trovare(placesClient = placesClient)
        }
    }
}
}




