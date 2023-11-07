package com.example.trovare.ui.theme.Recursos

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import com.example.trovare.ui.theme.Trv4
import com.example.trovare.ui.theme.Trv5
import kotlinx.coroutines.flow.emptyFlow


//DIVISOR-------------------------------------------------------------------------------------------
@Composable
fun Divisor(modifier: Modifier = Modifier){
    Divider(
        modifier = modifier
            .padding(horizontal = 25.dp, vertical = 15.dp),
        color = Color.White
    )
}

//BARRA-SUPERIOR------------------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(
    navController: NavController
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(Trv1),
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        title = {
        }
    )
}

//BARRA-SUPERIOR-CON-CONFIGURACION------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperiorConfig(navController: NavController) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(Trv1),
        actions = {
            IconButton(onClick = { navController.navigate(Pantalla.Configuracion.ruta) }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        },
        title = {
        }
    )
}

//VENTANA-DE-ALERTA---------------------------------------------------------------------------------
@Composable
fun VentanaDeAlerta(
    mostrar: Boolean,
    alRechazar: () -> Unit,
    alConfirmar: () -> Unit,
    textoRechazar: String = "Cancelar",
    textoConfirmar: String,
    titulo: String,
    texto: String,
    icono: ImageVector,
    colorConfirmar: Color = Color.White
) {
    if(mostrar){
        AlertDialog(
            containerColor = Trv2,
            icon = {
                Icon(
                    imageVector = icono,
                    contentDescription = "",
                    tint = Color.White
                )
            },
            title = {
                Text(
                    text = titulo,
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = texto,
                    textAlign = TextAlign.Justify,
                    color = Color.White
                )
            },
            onDismissRequest = {
                alRechazar()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        alConfirmar()
                    }
                ) {
                    Text(
                        text = textoConfirmar,
                        color = colorConfirmar
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        alRechazar()
                    }
                ) {
                    Text(
                        text = textoRechazar,
                        color = Color.White
                    )
                }
            }
        )
    }

}
//Menu de navegacion inferior-----------------------------------------------------------------------

@Composable
fun MenuInferior(
    modifier: Modifier = Modifier,
    presionarHome: () -> Unit,
    presionarPerfil: () -> Unit,
    presionarNavegacion: () -> Unit,
    presionarItinerario: () -> Unit,
    colorHome: Color = Color.White,
    colorPerfil: Color = Color.White,
    colorNavegacion: Color = Color.White,
    colorItinerario: Color = Color.White,

) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Trv1
    ) {
        Card(
            modifier = modifier.padding(horizontal = 30.dp, vertical = 15.dp),
            colors = CardDefaults.cardColors(Trv4)

        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                IconButton(onClick = { presionarHome() }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.Home,
                        contentDescription =  "",
                        tint = colorHome
                    )
                }
                IconButton(onClick = { presionarPerfil() }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription =  "",
                        tint = colorPerfil
                    )
                }
                IconButton(onClick = { presionarNavegacion() }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.Explore,
                        contentDescription =  "",
                        tint = colorNavegacion
                    )
                }
                IconButton(onClick = { presionarItinerario() }) {
                    Icon(
                        modifier = modifier
                            .size(40.dp),
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription =  "",
                        tint = colorItinerario
                    )
                }
            }
        }

    }
}


//No mostrar feedback al interactuar con una tarjeta------------------------------------------------
class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: kotlinx.coroutines.flow.Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}
