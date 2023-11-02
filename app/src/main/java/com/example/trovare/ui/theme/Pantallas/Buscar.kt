package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.TravelExplore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.PantallasTrovare
import com.example.trovare.ui.theme.Data.listaDePreguntas
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.NoRippleInteractionSource
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Buscar(
    modifier: Modifier = Modifier,
    navController: NavController
){
    val focusRequester = remember { FocusRequester() }
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    
    LaunchedEffect(key1 = Unit){
        focusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            Surface(
                modifier = modifier
                    .wrapContentSize(),
                color = Trv1
            ){
                Column {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            modifier = modifier.padding(start = 15.dp, top = 15.dp),
                            onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowLeft,
                                    contentDescription = "",
                                    tint = Color.White
                                )
                        }
                        //Barra de Búsqueda---------------------------------------------------------
                        Card(
                            modifier = modifier
                                .padding(start = 25.dp, end = 25.dp, top = 15.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(Color.Black),
                            border = CardDefaults.outlinedCardBorder()
                        ){
                            TextField(
                                modifier = modifier
                                    .focusRequester(focusRequester)
                                    .fillMaxWidth(),
                                value = textoBuscar,
                                onValueChange = { textoBuscar = it },
                                leadingIcon = {Icon(imageVector = Icons.Rounded.TravelExplore, contentDescription = "")},
                                textStyle = MaterialTheme.typography.labelSmall,
                                placeholder = { Text(text = "Busca lugares de interés", style = MaterialTheme.typography.labelSmall) },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.White,
                                    containerColor = Color.Black,
                                    cursorColor = Color.White,
                                )
                            )
                        }
                    }
                    Divisor()
                }
            }

        },
    ){
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ){
            LazyColumn(){
                item {
                    //contenido de la pag
                }
            }
        }
    }


}
@Composable
fun BarrInferiornueva(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Trv3
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 15.dp, bottom = 15.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Servicio al cliente",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
            Text(
                text = "5555-5555",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Black
            )
        }

    }
}


