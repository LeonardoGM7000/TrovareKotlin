package com.example.trovare.ui.theme.Recursos

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.trovare.PantallasTrovare
import com.example.trovare.R
import com.example.trovare.ui.theme.Data.listaDeExplorar
import com.example.trovare.ui.theme.Data.listaDePreguntas
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv2
import kotlinx.coroutines.flow.emptyFlow
import java.util.concurrent.Flow
import kotlin.math.absoluteValue

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
            IconButton(onClick = { navController.navigate(PantallasTrovare.Configuracion.name) }) {
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

//No mostrar feedback al interactuar con una tarjeta------------------------------------------------
class NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: kotlinx.coroutines.flow.Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true

}
