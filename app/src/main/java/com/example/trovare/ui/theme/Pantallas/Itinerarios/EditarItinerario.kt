package com.example.trovare.ui.theme.Pantallas.Itinerarios

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.trovare.R
import com.example.trovare.ViewModel.TrovareViewModel
import com.example.trovare.ui.theme.CalendarTheme
import com.example.trovare.ui.theme.JosefinSans
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv11
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarItinerario(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: TrovareViewModel
){

    val itinerario by viewModel.itinerarioActual.collectAsState()
    var nombreItinerario by remember { mutableStateOf(itinerario.nombre) }
    var publico by rememberSaveable { mutableStateOf(false) }
    var lugares by remember { mutableStateOf(itinerario.lugares) }
    val calendarState = rememberSheetState()
    val clockState = rememberSheetState()

    CalendarTheme {
        CalendarDialog(
            state = calendarState,
            selection = CalendarSelection.Date{ fecha->
                //viewModel.setFechaLugar(fecha)
            },
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true
            )
        )
    }

    //CalendarTheme {
        ClockDialog(
            state = clockState,
            selection = ClockSelection.HoursMinutes{hours, minutes ->
                //TODO
            }
        )
    //}

    Scaffold(
        topBar = {BarraSuperior(navController = navController)}
    ) {
        Surface(
            modifier = modifier
                .padding(it)
                .fillMaxSize(),
            color = Trv1
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                item {
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Editar Itinerario",
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                item {
                    Divisor()
                }
                item {
                    TextField(
                        modifier = modifier
                            .padding(horizontal = 25.dp),
                        value = nombreItinerario,
                        onValueChange = {nuevoNombre->
                            nombreItinerario = nuevoNombre
                            viewModel.setNombreItinerario(nuevoNombre)
                        },
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontFamily = JosefinSans,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        ),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            containerColor = Trv1,
                            focusedIndicatorColor = Trv1,
                            unfocusedIndicatorColor = Trv1,
                            cursorColor = Color.White,
                        )
                    )
                }
                item{
                    Card(
                        modifier = modifier
                            .padding(vertical = 10.dp)
                            .size(200.dp)
                            .aspectRatio(1F),
                    ){
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomEnd
                        ){
                            Image(
                                modifier = modifier
                                    .fillMaxSize(),
                                painter = painterResource(id = R.drawable.image_placeholder),
                                contentDescription = ""
                            )
                            FloatingActionButton(
                                modifier = modifier
                                    .padding(5.dp)
                                    .size(30.dp),
                                onClick = { /*navController.popBackStack()*/ },
                                containerColor = Color.White,
                                shape = CircleShape
                            ){
                                Icon(
                                    imageVector = Icons.Rounded.Edit,
                                    contentDescription = "",
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Público",
                            textAlign = TextAlign.Left,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Switch(
                            checked = publico,
                            onCheckedChange = { publico = !publico },
                            thumbContent = {
                                if (publico){
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = ""
                                    )
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Trv11,
                                checkedThumbColor = Trv6,
                                checkedIconColor = Color.Black
                            )
                        )
                    }
                }
                item {
                    Divisor()
                }
                item {
                    Card(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp)
                            .clickable {
                                //calendar State.show()
                                navController.navigate(Pantalla.AgregarLugarItinerario.ruta)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = Trv3
                        )
                    ) {
                        Row (
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                modifier = modifier.padding(5.dp),
                                text = "Agregar lugar",
                                textAlign = TextAlign.Left,
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Icon(
                                modifier = modifier.padding(5.dp),
                                imageVector = Icons.Rounded.Place,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                }
                item {
                    Divisor()
                }
                if(lugares == null){
                    item {
                        Box(modifier = modifier
                            .fillMaxSize()
                            .padding(horizontal = 25.dp)
                        ){
                            Text(
                                modifier = modifier.fillMaxSize(),
                                text = "No hay actividades en tu itinerario",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Left,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    items(lugares!!){lugar ->

                        Card(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp, vertical = 5.dp)
                                .size(100.dp)
                                .clickable {
                                    //TODO
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = Trv3
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Card(
                                    modifier = modifier
                                        .padding(5.dp)
                                        .aspectRatio(1f),
                                ) {
                                    Image(
                                        modifier = modifier
                                            .fillMaxSize(),
                                        painter = painterResource(id = R.drawable.image_placeholder),
                                        contentDescription = ""
                                    )
                                }
                                Column {
                                    Text(
                                        text = lugar.nombreLugar,
                                        color = Color.Black,
                                        maxLines = 2
                                    )
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Row(
                                            modifier = modifier
                                                .clickable {
                                                    calendarState.show()
                                                },
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Icon(
                                                imageVector = Icons.Rounded.CalendarToday,
                                                contentDescription = "",
                                                tint = Color.Black
                                            )
                                            Text(
                                                text = "Editar",
                                                color = Color.Black,
                                                fontSize = 20.sp
                                            )
                                        }
                                        Spacer(
                                            modifier = modifier.padding(horizontal = 10.dp)
                                        )
                                        Row(
                                            modifier = modifier
                                                .clickable {
                                                    clockState.show()
                                                },
                                            verticalAlignment = Alignment.CenterVertically
                                        ){
                                            Icon(
                                                imageVector = Icons.Rounded.AccessTime,
                                                contentDescription = "",
                                                tint = Color.Black
                                            )
                                            Text(
                                                text = "Editar",
                                                color = Color.Black,
                                                fontSize = 20.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}