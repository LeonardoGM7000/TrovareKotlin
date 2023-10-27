package com.example.trovare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
//import com.example.trovare.ui.theme.Navegacion.Nav
import com.example.trovare.ui.theme.Pantallas.Inicio
import com.example.trovare.ui.theme.TrovareTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
//import com.example.trovare.ui.theme.Navegacion.Nav
import com.example.trovare.ui.theme.Pantallas.Configuracion
import com.example.trovare.ui.theme.Pantallas.FAQS
import com.example.trovare.ui.theme.Pantallas.Soporte

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TrovareTheme {
                // A surface container using the 'background' color from the theme
                Configuracion()
        }
    }
}
}

@Preview
@Composable
fun revision(){
    TrovareTheme {
        Configuracion()
    }

}


