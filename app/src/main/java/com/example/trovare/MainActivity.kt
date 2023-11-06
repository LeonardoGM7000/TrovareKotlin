package com.example.trovare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.trovare.ui.theme.Pantallas.Detalles
import com.example.trovare.ui.theme.TrovareTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*colocar aqui API de google places*/
        Places.initialize(this, /*colocar aqui llave de API de google places*/"")//Inicializar API de Places
        val placesClient: PlacesClient = Places.createClient(this)//Crear cliente

        setContent {

            TrovareTheme {
                Trovare(placesClient = placesClient)
        }
    }
}
}




