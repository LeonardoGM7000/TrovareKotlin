package com.example.trovare.ui.theme.Pantallas

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.ui.theme.Navegacion.Pantalla
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


data class Cuenta(val nombre: String = "", val id: String="")

var effectKey = 0
val cuentasSeleccionadas = mutableListOf<Cuenta>()
var snackbarFlag = mutableStateOf(0)



@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarCuentas(
    modifier: Modifier = Modifier,
    navController: NavController,
    buscar: String = ""
){
    //snackbarFlag = 0
    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    var cuentas by remember { mutableStateOf(emptyList<Cuenta>()) }
    var expandedCuentaIndex by remember { mutableIntStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    //var snackbarVisible by remember { mutableStateOf(false) }
    //var snackbarMessage by remember { mutableStateOf("") }

    //var flagstate by remember {
      //  mutableIntStateOf(0)
    //}
    Log.i("entro","entro")


    LaunchedEffect(effectKey) {
        //snackbarFlag =0;
        Log.i("entro","entro")
        Log.i("EliminarCuentas", "LaunchedEffect: Obtaining accounts from Firestore")
        try {
            val cuentasSnapshot = firestore.collection("Usuario").get().await()
            cuentas = cuentasSnapshot.documents.map { document ->
                val cuenta = document.toObject(Cuenta::class.java)
                cuenta?.copy(id = document.id) ?: Cuenta() // Asigna el ID al objeto Cuenta
            }

            Log.i("EliminarCuenta", "Cuentas obtenidas exitosamente: $cuentas")
        } catch (e: Exception) {
            Log.e("EliminarCuenta", "Error al obtener cuentas de Firestore", e)
        }finally {
            isLoading = false
        }
    }

    LaunchedEffect(snackbarFlag){
        //flagstate = 1
        if (snackbarFlag.value == 1) {
            Log.i("Mensaje Entro verificacion", "Entro busqueda Cuenta")
            // Muestra el Snackbar con el mensaje de éxito
            delay(500)
            scope.launch {
                Log.i("Mensaje ENtro snackbar", "SnackBar si se va a mostrar")
                snackbarHostState
                    .showSnackbar(
                        message = "Cuentas borradas con exito",
                        duration = SnackbarDuration.Short
                    )
                snackbarFlag.value = 0;

            }

            Log.i("Actualizacion snackbarflag", "Snackbarfalg: $snackbarFlag")

        }
    }

    Log.i("EliminarCuenta","SnackBar afuera: $snackbarFlag");


    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
    ) { it ->

        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
            if(isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }
            } else {


                LazyColumn() {
                    item {
                        TituloAdmin(titulo = "ELIMINAR CUENTAS")
                    }
                    item {
                        Divisor(modifier = modifier.padding(15.dp))
                    }

                    item {
                        Log.i("Mensaje Entro Busqueda", "Entro busqueda Cuenta")
                        BusquedaCuenta(navController = navController);
                    }

                    Log.i("Mensaje Eliminar cuenta", buscar)
                    Log.i("Total cuentas", cuentas.size.toString())
                    if (buscar.isNotBlank()) {
                        Log.i("mensaje entro", "entro al effectkey")
                        effectKey++
                    }

                    //snackbarFlag = 0;
                    Log.i("mensaje entro siguiente", "entro al effectkey")
                    try {
                        items(cuentas.size) { index ->
                            val cuenta = cuentas[index]
                            if ((Buscar == cuenta.nombre) || Buscar.isBlank()) {
                                Log.i("Si hay cuentas", Buscar)
                                TarjetaUsuario(
                                    cuenta = cuenta,
                                    modifier = modifier.padding(top = 20.dp),
                                    navController = navController
                                 )
                            } else {
                                Log.i("No hay cuentas", Buscar)
                            }
                        }
                    } catch (e: Exception) {
                        Log.i("No hay cuentas", Buscar)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaUsuario(
    cuenta: Cuenta,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
    var checked = remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 15.dp, vertical = 5.dp),
            colors = CardDefaults.cardColors(
                containerColor = Trv1
            )
        )
        {
            Row() {
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    /*Image(
                        painter = painterResource(id = cuenta.fotoperfil),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )*/
                }
                Text(
                    text = cuenta.nombre,
                    modifier = Modifier.padding(18.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Trv3,
                )
            }
        }
        Checkbox(
            modifier = Modifier.padding(end = 13.dp),
            checked = checked.value,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    // Agregar la cuenta a la lista de cuentas seleccionadas si el Checkbox está marcado
                    cuentasSeleccionadas.add(cuenta)
                    Log.i("SE AÑADIO CORRECTAMENTE: cuenta.nombre",cuenta.nombre);
                } else {
                    // Quitar la cuenta de la lista de cuentas seleccionadas si el Checkbox se desmarca
                    cuentasSeleccionadas.remove(cuenta)
                    Log.i("SE QUITO DE LA LISTA: cuenta.nombre",cuenta.nombre);
                }
                // Actualizar el valor de checked.value

                checked.value = isChecked
            },
            colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaCuenta(
    modifier: Modifier = Modifier,
    navController: NavController
){
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }
    val focusRequester =
        remember { FocusRequester() }//permite que se abra el teclado automaticamente al abrir la pantalla
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    SnackbarHost(
        hostState = snackbarHostState
        //modifier = Modifier.align(Alignment.BottomCenter)
    )

    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//saber si se esta llevando a cabo una busqueda en el momento(permite mostrar el indicador de progreso circular)
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }
    var job: Job? by remember { mutableStateOf(null) }
    val onDoneClick: () -> Unit = {
        // Aquí llama a la función que deseas ejecutar al hacer clic en la palomita
        println("Texto ingresado: $textoBuscar")
        effectKey++
        Buscar = textoBuscar.text
        navController.navigate(Pantalla.EliminarCuentas.ruta){
            popUpTo(Pantalla.EliminarCuentas.ruta){
                inclusive = true
            }
        }
    }
    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            busquedaEnProgreso = true//establece que hay una busqueda en progreso (para el indicador de progreso)
            while (tiempoRestante > 0) {
                delay(1000)
                tiempoRestante--//resta 1 al contador de tiempo, lo que quiere decir que ha pasado un segundo
            }
            busquedaEnProgreso = false//se acaba el tiempo del timer y se lleva a cabo la busqueda
            //Log.i("test", "terminado")
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Card(
            modifier = modifier
                .padding(start = 25.dp),
            colors = CardDefaults.cardColors(Color.Black),
            border = CardDefaults.outlinedCardBorder()
        ){
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester),
                value = textoBuscar,
                onValueChange = {
                    textoBuscar = it
//                    Log.i("Texto escrito", textoBuscar.text)
                    job?.cancel() // Cancela la corrutina actual si es que existe
                    tiempoRestante = 1//resetea el timer a 1 segundo
                    iniciarTimer()//reinicia la cuenta regresiva del timer
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Llama a la función cuando se presiona la tecla de verificación
                        //EliminarCuentas(navController = navController,
                        //modifier = Modifier, buscar = textoBuscar.text)
                        onDoneClick.invoke()
                    }
                ),
                leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = "") },
                textStyle = MaterialTheme.typography.labelSmall,
                placeholder = {
                    Text(
                        text = "Buscar Cuenta",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Black,
                    cursorColor = Color.White,
                )
            )
            //Log.i("Texto escrito", textoBuscar.text)
        }
        Log.i("Texto escrito en la función Buscar", textoBuscar.text)
        /*if(textoBuscar.text.isNotBlank())  EliminarCuentas(navController = navController,
            modifier = modifier.padding(top = 20.dp), buscar = textoBuscar.text)*/

        Icon(
            modifier = Modifier
                .padding(25.dp)
                .size(28.dp)
                .clickable { mostrarBorrarCuenta = true },
            imageVector = Icons.Default.Delete,
            contentDescription = "",
            tint = Color.Red,
        )
        VentanaDeAlerta(
            mostrar = mostrarBorrarCuenta,
            alRechazar = {mostrarBorrarCuenta = false},
            alConfirmar = {

                eliminarCuentasSeleccionadas(cuentasSeleccionadas)

                navController.navigate(Pantalla.EliminarCuentas.ruta) {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
                Log.i("cuentas", cuentasSeleccionadas.toString())
            },

            textoConfirmar = "Eliminar Cuentas",
            titulo = "Eliminar Cuentas",
            texto = "¿Estás seguro de eliminar todas las cuentas seleccionadas?",
            icono = Icons.Filled.DeleteForever,
            colorConfirmar = Color.Red
        )


    }
}
fun eliminarCuentasSeleccionadas(cuentasSeleccionadas: List<Cuenta>) {

    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
     try {
        for (cuenta in cuentasSeleccionadas) {
            firestore.collection("Usuario").document(cuenta.id).delete()

        }
         snackbarFlag.value =1;
    } catch (e: Exception) {
        // Manejar el error, por ejemplo, mostrar un mensaje al usuario
         snackbarFlag.value = 2
    }
}

