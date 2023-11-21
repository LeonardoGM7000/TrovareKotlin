package com.example.trovare

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.trovare.ui.theme.TrovareTheme
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {

    private val KEY_EMAIL = "correo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*colocar aqui API de google places*/
        Places.initialize(this, /*colocar aqui llave de API de google places*/"AIzaSyBpmAJRF6PsRJVNm6oq1qmfXbdaBjNA5mQ")//Inicializar API de Places
        val placesClient: PlacesClient = Places.createClient(this)//Crear cliente

        // Verificamos si el usuario ya inicio sesión previamente
        val sharedPreferecnes = getSharedPreferences("DB", MODE_PRIVATE)
        val editor = sharedPreferecnes.edit()

        // Creamos una instancia de firebase para verificar el usuario registrado
        val auth = FirebaseAuth.getInstance()

        val correo = sharedPreferecnes.getString(KEY_EMAIL, null)

        // Variable que almacena la ruta de la pantalla
        var ruta = ""

        try{

            Log.d("Main_Trovare", correo.toString())

            if(correo != null){

                Log.d("Main_Trovare", "Pantalla Inicio")
                ruta = Pantalla.Inicio.ruta

            }else{
                Log.d("Main_Trovare", "Pantalla Bienvenida")
                ruta = Pantalla.Bienvenida.ruta
            }

            editor.putString(KEY_EMAIL, auth.currentUser?.email.toString())
            editor.apply()

        }catch(e:Exception){

            Log.d("Main_Trovare", "Error en la conexión de la base de datos")
        }


        setContent {
            TrovareTheme {
                Trovare(
                    placesClient = placesClient,
                    destino = ruta,
                    context = this
                    )
        }
    }
}
}




