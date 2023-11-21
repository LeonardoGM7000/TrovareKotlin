<<<<<<< HEAD
package com.example.trovare.ui.theme.Pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
=======

package com.example.trovare.ui.theme.Pantallas

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
<<<<<<< HEAD
=======
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
<<<<<<< HEAD
=======
import androidx.compose.material3.CircularProgressIndicator
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
<<<<<<< HEAD
=======
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
<<<<<<< HEAD
=======
import androidx.compose.runtime.LaunchedEffect
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
<<<<<<< HEAD
=======
import androidx.compose.runtime.rememberCoroutineScope
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
<<<<<<< HEAD
=======
import androidx.compose.ui.text.input.ImeAction
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trovare.Pantalla
<<<<<<< HEAD
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.example.trovare.ui.theme.Navegacion.TrovareViewModel
=======
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import com.example.trovare.ui.theme.Recursos.BarraSuperior
import com.example.trovare.ui.theme.Recursos.Divisor
import com.example.trovare.ui.theme.Recursos.VentanaDeAlerta
import com.example.trovare.ui.theme.Trv1
import com.example.trovare.ui.theme.Trv3
import com.example.trovare.ui.theme.Trv6
<<<<<<< HEAD
=======
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
<<<<<<< HEAD


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarCuentas(
    modifier: Modifier = Modifier,
    navController: NavController
){
=======
import kotlinx.coroutines.tasks.await

data class Cuenta(val nombre: String = "")
var effectKey = 0
val cuentasSeleccionadas = mutableListOf<Cuenta>()

@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EliminarCuentas(
    modifier: Modifier = Modifier.fillMaxWidth(),
    navController: NavController,
    buscar: String = ""
) {
    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    var cuentas by remember { mutableStateOf(emptyList<Cuenta>()) }
    var expandedCuentaIndex by remember { mutableIntStateOf(-1) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Log.i("entro inicial", Buscar)
    LaunchedEffect(effectKey) {
        Log.i("entro", "entro")
        Log.i("EliminarCuentas", "LaunchedEffect: Obtaining accounts from Firestore")
        try {
            val cuentasSnapshot = firestore.collection("Usuario").get().await()
            cuentas = cuentasSnapshot.toObjects(Cuenta::class.java)
            Log.i("EliminarCuenta", "Accounts obtained successfully: $cuentas")
        } catch (e: Exception) {
            Log.i("EliminarCuenta", "Error obtaining accounts from Firestore", e)
        } finally {
            isLoading = false
        }
    }
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
    Scaffold(
        topBar = {
            BarraSuperior(navController = navController)
        },
    ) { it ->
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = Trv1
        ) {
<<<<<<< HEAD
            LazyColumn() {
                item {
                    TituloAdmin(titulo = "ELIMINAR COMENTARIOS")
                }
                item {
                    Divisor( modifier = modifier.padding(15.dp))
                }
                item {
                    BusquedaCuenta(navController = navController)
                }
                item {
                    TarjetaUsuario(usuario = usuarioPrueba)
=======
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
                        BusquedaCuenta(navController = navController)
                    }
                    Log.i("Mensaje Eliminar cuenta", buscar)
                    Log.i("Total cuentas", cuentas.size.toString())
                    if (buscar.isNotBlank()) {
                        Log.i("mensaje entro", "entro al effectkey")
                        effectKey++
                    }
                    Log.i("mensaje entro siguiente", "entro al effectkey")
                    try {
                        items(cuentas.size) { index ->
                            val cuenta = cuentas[index]
                            if ((Buscar == cuenta.nombre) || Buscar.isBlank()) {
                                Log.i("Si hay cuentas", Buscar)

                                TarjetaUsuario(
                                    cuenta = cuenta,
                                    expanded = index == expandedCuentaIndex,
                                    onDeleteClick = {
                                        if(cuentasSeleccionadas.contains(cuenta)){
                                            Log.i("Entro a eliminar","entro a eliminar")
                                            firestore.collection("Usuario").document(cuenta.nombre)
                                                .delete()
                                                .addOnSuccessListener {
                                                    // Éxito al eliminar
                                                    Log.d("EliminarCuenta", "Cuenta eliminada")
                                                }
                                                .addOnFailureListener { e ->
                                                    // Error al eliminar
                                                    Log.w("EliminarCuenta", "Error al eliminar cuenta", e)
                                                }
                                            cuentas = cuentas.filterNot { it.nombre == cuenta.nombre}
                                            cuentasSeleccionadas.clear();
                                        }
                                        // Eliminar la pregunta de Firestore

                                        //  .delete()
                                        //accounts = accounts.filterNot { it.nombre == account.nombre }
                                    },
                                    modifier = modifier.padding(top = 20.dp),
                                    navController = navController
                                )
                            } else {
                                Log.i("No hay cuentas", Buscar)
                                /* scope.launch {
                                 snackbarHostState.showSnackbar(
                                     message = "El correo ingresado ya está asociado a otra cuenta",
                                     duration = SnackbarDuration.Short
                                 )
                             }*/
                            }
                            0                        }
                    } catch (e: Exception) {
                        Log.i("No hay cuentas", Buscar)
                    }
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
                }
            }
        }
    }
}

<<<<<<< HEAD
=======

@Composable
fun TarjetaUsuario(
    cuenta: Cuenta,
    expanded: Boolean,
    onDeleteClick: () -> Unit,
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
                    Log.i("SE AÑADIO CORRECTAMENTE LA CUENTA: cuenta.nombre",cuenta.nombre);
                } else {
                    // Quitar la cuenta de la lista de cuentas seleccionadas si el Checkbox se desmarca
                    cuentasSeleccionadas.remove(cuenta)
                    Log.i("SE QUITO DE LA LISTA LA CUENTA: cuenta.nombre",cuenta.nombre);
                }
                // Actualizar el valor de checked.value

                checked.value = isChecked
            },
            colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1)
        )
    }
}

>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusquedaCuenta(
    modifier: Modifier = Modifier,
    navController: NavController
<<<<<<< HEAD
){
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }//permite que se abra el teclado automaticamente al abrir la pantalla
=======
) {
    var mostrarBorrarCuenta by rememberSaveable { mutableStateOf(false) }

    val focusRequester =
        remember { FocusRequester() }//permite que se abra el teclado automaticamente al abrir la pantalla
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
    var textoBuscar by rememberSaveable(stateSaver = TextFieldValue.Saver) {//texto a buscar
        mutableStateOf(TextFieldValue("", TextRange(0, 7)))
    }

    var busquedaEnProgreso by rememberSaveable { mutableStateOf(false) }//saber si se esta llevando a cabo una busqueda en el momento(permite mostrar el indicador de progreso circular)
    var tiempoRestante by rememberSaveable { mutableIntStateOf(1) }

    var job: Job? by remember { mutableStateOf(null) }
<<<<<<< HEAD

    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            busquedaEnProgreso = true//establece que hay una busqueda en progreso (para el indicador de progreso)
=======
    val onDoneClick: () -> Unit = {
        // Aquí llama a la función que deseas ejecutar al hacer clic en la palomita
        println("Texto ingresado: $textoBuscar")
        effectKey++
        Buscar = textoBuscar.text
        navController.navigate(Pantalla.EliminarCuentas.ruta)
    }

    fun iniciarTimer() {
        job = CoroutineScope(Dispatchers.Default).launch {
            busquedaEnProgreso =
                true//establece que hay una busqueda en progreso (para el indicador de progreso)
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264

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
<<<<<<< HEAD
    ){
=======
    ) {
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
        Card(
            modifier = modifier
                .padding(start = 25.dp),
            colors = CardDefaults.cardColors(Color.Black),
            border = CardDefaults.outlinedCardBorder()
<<<<<<< HEAD
        ){
=======
        ) {
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
            TextField(
                modifier = modifier
                    .focusRequester(focusRequester),
                value = textoBuscar,
                onValueChange = {
                    textoBuscar = it
<<<<<<< HEAD
=======
//                    Log.i("Texto escrito", textoBuscar.text)
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
                    job?.cancel() // Cancela la corrutina actual si es que existe
                    tiempoRestante = 1//resetea el timer a 1 segundo
                    iniciarTimer()//reinicia la cuenta regresiva del timer
                },
<<<<<<< HEAD
                leadingIcon = { Icon(imageVector = Icons.Rounded.Search, contentDescription = "") },
                textStyle = MaterialTheme.typography.labelSmall,
                placeholder = { Text(text = "Buscar Cuenta", style = MaterialTheme.typography.labelSmall) },
=======
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
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    containerColor = Color.Black,
                    cursorColor = Color.White,
                )
            )
<<<<<<< HEAD
        }
=======
            //Log.i("Texto escrito", textoBuscar.text)
        }
        Log.i("Texto escrito en la función Buscar", textoBuscar.text)
        //if(textoBuscar.text.isNotBlank())  EliminarCuentas(navController = navController,
        //  modifier = Modifier, buscar = textoBuscar.text)

>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
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
<<<<<<< HEAD
            alRechazar = {mostrarBorrarCuenta = false},
            alConfirmar = { //Necesita eliminar la pregunta
                navController.navigate(Pantalla.Administrador.ruta){
                    popUpTo(navController.graph.id){
=======
            alRechazar = { mostrarBorrarCuenta = false },
            alConfirmar = {
                //Necesita eliminar la pregunta
                //borrarCuentas()
                navController.navigate(Pantalla.Administrador.ruta) {
                    popUpTo(navController.graph.id) {
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
                        inclusive = true
                    }
                }
            },
            textoConfirmar = "Eliminar Cuentas",
            titulo = "Eliminar Cuentas",
            texto = "¿Estás seguro de eliminar todas las cuentas seleccionadas?",
            icono = Icons.Filled.DeleteForever,
            colorConfirmar = Color.Red
        )
    }
<<<<<<< HEAD
}

@Composable
fun TarjetaUsuario(
    modifier: Modifier = Modifier,
    usuario: Usuario = usuarioPrueba,
){

    var checked = remember { mutableStateOf(true) }

   Row(
       modifier = Modifier
           .padding(15.dp)
           .fillMaxWidth(),
       verticalAlignment = Alignment.CenterVertically,
       horizontalArrangement = Arrangement.SpaceBetween
   ) {
       Card(modifier = Modifier
           .padding(horizontal = 15.dp, vertical = 5.dp),
           colors = CardDefaults.cardColors(
               containerColor = Trv1
           ))
           {
           Row() {
               Card(
                   modifier = Modifier
                       .padding(10.dp)
                       .size(50.dp),
                   shape = RoundedCornerShape(100.dp)
               ) {
                   Image(
                       painter = painterResource(id = usuario.foto_perfil),
                       contentDescription = "",
                       contentScale = ContentScale.FillBounds
                   )
               }
               Text(
                   text = usuario.nombre,
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
           onCheckedChange = {checked.value = it},
           colors = CheckboxDefaults.colors(Trv6, Color.White, Trv1))
   }
}
=======
}
>>>>>>> bf972f322efdd1ef70f150421b2f2df11170b264
