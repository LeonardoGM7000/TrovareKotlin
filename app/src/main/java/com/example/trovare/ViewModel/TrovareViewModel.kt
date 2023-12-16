package com.example.trovare.ViewModel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trovare.Data.Hora
import com.example.trovare.Data.Itinerario
import com.example.trovare.Data.Lugar
import com.example.trovare.Data.Usuario
import com.example.trovare.Data.itinerarioPrueba
import com.example.trovare.Data.usuarioPrueba
import com.example.trovare.ui.theme.Pantallas.Mapa.MapState
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.time.LocalDate

/**
 * [TrovareViewModel] guarda información de la aplicación dentro del ciclo de vida.
 */
//F por Uzias

class TrovareViewModel : ViewModel() {


    //--------------------------------------------------------------------------------------------//
    //-------------------------------------IMAGENES-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //Manejo de imagenes----------------------------------------------------------------------------
    private val _imagen = mutableStateOf<ImageBitmap?>(null)
    val imagen: State<ImageBitmap?> = _imagen

    private val _imagenes = mutableStateOf(mutableListOf<ImageBitmap?>())
    val imagenes: State<MutableList<ImageBitmap?>?> = _imagenes

    fun reiniciarImagen() {
        _imagen.value = null
    }

    //--------------------------------------------------------------------------------------------//
    //------------------------------ESTADO DE LA UI-----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //guardar variables de estado de la UI----------------------------------------------------------

    private val _estadoUi = MutableStateFlow(TrovareEstadoUi())
    val uiState: StateFlow<TrovareEstadoUi> = _estadoUi.asStateFlow()

    fun setIdioma(nuevoIdioma: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                idioma = nuevoIdioma,
            )
        }
    }

    fun setUnidades(nuevaUnidad: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                unidad = nuevaUnidad,
            )
        }
    }

    fun setMonedas(nuevaMoneda: String) {
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                moneda = nuevaMoneda,
            )
        }
    }

    fun setResultoUtil(nuevaSeleccion: String){
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                resultoUtil = nuevaSeleccion
            )
        }
    }

    //--------------------------------------------------------------------------------------------//
    //-------------------------------------UBICACIÓN----------------------------------------------//
    //--------------------------------------------------------------------------------------------//

    //Ubicacion-------------------------------------------------------------------------------------

    val state: MutableState<MapState> = mutableStateOf(
        MapState(
            lastKnownLocation = null,
        )
    )
    //Obtener ubicacion en tiempo real
    @SuppressLint("MissingPermission")
    fun getDeviceLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    state.value = state.value.copy(
                        lastKnownLocation = task.result,
                    )
                }
            }
        } catch (e: SecurityException) {
            //TODO
        }
    }
    //Obtener ubicacion en temporal en LatLng y cargarla al viewmodel
    fun getLastLocation(
        fusedLocationProviderClient: FusedLocationProviderClient
    ) {
        try {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        // Aquí tienes la latitud y longitud.
                        // Puedes usar estas variables en tu lógica o pasárselas al ViewModel según sea necesario.
                        setUbicacion(LatLng(latitude, longitude))

                    }
                }
                .addOnFailureListener { exception ->
                    //TODO
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
    //ubicaci[on del usuario
    private val _ubicacion = MutableStateFlow(LatLng(19.504507, -99.147314))
    val ubicacion = _ubicacion.asStateFlow()
    // Función para actualizar el valor de la ubicación
    fun setUbicacion(nuevaUbicacion: LatLng) {
        _ubicacion.value = nuevaUbicacion
    }
    //mostrar la polilinea de la ruta
    //Guardar la polilinea codificada
    private val _polilineaCod = MutableStateFlow("")
    val polilineaCod = _polilineaCod.asStateFlow()
    fun setPolilineaCod(newValue: String) {
        _polilineaCod.value = newValue
    }

    //para mostrar el marcador de un solo lugar
    private val _marcadorInicializado = MutableStateFlow(false)
    val marcadorInicializado: StateFlow<Boolean> = _marcadorInicializado.asStateFlow()
    fun setMarcadorInicializado(newValue: Boolean) {
        _marcadorInicializado.value = newValue
    }
    //mostrar los marcadores de varios lugares
    private val _marcadoresInicializado = MutableStateFlow(false)
    val marcadoresInicializado: StateFlow<Boolean> = _marcadoresInicializado.asStateFlow()
    fun setMarcadoresInicializado(newValue: Boolean) {
        _marcadoresInicializado.value = newValue
    }
    //mostrar la tarjeta de inofmracion del lugar
    private val _informacionInicializada = MutableStateFlow(false)
    val informacionInicializada: StateFlow<Boolean> = _informacionInicializada.asStateFlow()
    fun setInformacionInicializada(newValue: Boolean) {
        _informacionInicializada.value = newValue
    }

    //mostrar la polilinea de la ruta
    private val _polilineaInicializada = MutableStateFlow(false)
    val polilineaInicializada: StateFlow<Boolean> = _polilineaInicializada.asStateFlow()
    fun setPolilineaInicializada(newValue: Boolean) {
        _polilineaInicializada.value = newValue
    }

    //nombre del lugar seleccionando
    private val _nombreLugar = MutableStateFlow("")
    val nombreLugar = _nombreLugar.asStateFlow()

    fun setNombreLugar(nuevoNombre: String) {
        _nombreLugar.value = nuevoNombre
    }
    //rating del lugar seleccionado
    private val _ratingLugar = MutableStateFlow(-1.0)
    val ratingLugar = _ratingLugar.asStateFlow()

    fun setRatingLugar(nuevoRating: Double) {
        _ratingLugar.value = nuevoRating
    }
    //id del lugar seleccionado
    private val _idLugar = MutableStateFlow("")
    val idLugar = _idLugar.asStateFlow()

    fun setIdLugar(nuevoId: String) {
        _idLugar.value = nuevoId
    }
    //--------------------------------------------------------------------------------------------//
    //-------------------------------------ITINERARIOS--------------------------------------------//
    //--------------------------------------------------------------------------------------------//
    //itinerario Actual
    private val _itinerarioActual = MutableStateFlow(itinerarioPrueba)
    val itinerarioActual = _itinerarioActual.asStateFlow()

    fun setItinerarioActual(nuevoItinerario: Itinerario) {
        _itinerarioActual.value = nuevoItinerario
    }
    fun setNombreItinerario(nuevoNombre: String){
        _itinerarioActual.value.nombre = nuevoNombre
    }

    fun agregarLugarALItinerario(id: String, nombreLugar: String) {

        val lugarNuevo = Lugar(id, nombreLugar, fechaDeVisita = "", horaDeVisita = "", imagen = null)
        Log.d("guardar_lugar", itinerarioActual.value.id.toString())

        //guardarImagen()

        try {

            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()

            Log.i("guardar_lugar", "Guardando datos...")

            val documento_lugar = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                .collection("Itinerario").document(itinerarioActual.value.id.toString()).collection("Lugar")

            documento_lugar.get().addOnSuccessListener{ documento->

                    Log.d("guardar_lugar", "Datos guardados")
                    Log.d("guardar_lugar", itinerarioActual.value.id.toString())

                    documento_lugar.document(documento.size().toString()).set(lugarNuevo)

                    // Guardamos la imagen
                    guardarImagen(_imagen.value, documento.size().toString())
                    cargarLugar()

                }
                .addOnFailureListener {

                    Log.i("guardar_lugar", "Datos no guardados")
                }

        }catch(e: Exception){
            Log.i("guardar_lugar", "Error al conectar con la base de datosmsfnfsks")
        }

        //lugarNuevo.imagen = _imagen.value

        val itinerarioActualValor = _itinerarioActual.value

        // Verificar si la lista de lugares existe, si no, crearla
        if (itinerarioActualValor.lugares == null) {
            itinerarioActualValor.lugares = mutableListOf()
        }


        // Agregar el nuevo lugar a la lista de lugares del itinerario actual
        itinerarioActualValor.lugares?.add(lugarNuevo)

        // Actualizar el valor del itinerario actual en MutableStateFlow
        _itinerarioActual.value = itinerarioActualValor

        cargarLugar()
    }

fun guardarImagen(imagen: ImageBitmap?, id: String){

val storage = FirebaseStorage.getInstance()
val baos = ByteArrayOutputStream()

    try {

        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        imagen?.asAndroidBitmap()?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val dato = baos.toByteArray()

        val itinerario = "itinerario_${itinerarioActual.value.id.toString()}"

        val storageRef: StorageReference =
            storage.reference.child("itinerarios/${auth.currentUser?.email.toString()}/$itinerario/id_$id")
        val tarea = storageRef.putBytes(dato)

        tarea.addOnSuccessListener {

            Log.d("cargar_imagenIt", "Imagen cargada en firestore")
            storageRef.downloadUrl.addOnSuccessListener {uri ->

                val url = uri.toString()

                Log.d("cargar_imagenIt", url)

                // Guardar la referencia de la imagen en Firestore
                val lugar = firestore.collection("Usuario").document(auth.currentUser?.email.toString()).collection("Itinerario").document(itinerarioActual.value.id.toString())
                    .collection("Lugar").document(id)
                lugar.update("imagen", uri.toString())
                    .addOnSuccessListener {
                        // Éxito al guardar la referencia en Firestore
                        Log.i("Imagen_Itinerario", "Imagen cargada con éxito")
                        cargarLugar()
                    }
                    .addOnFailureListener { e ->
                        // Error al guardar la referencia en Firestore
                        Log.i("Imagen_Itinerario", "Error al guardar la imagen", e)
                    }
            }


        }
            .addOnFailureListener {

                Log.d("cargar_imagenIt", "Errooooooor")
            }

    }catch (e: Exception){

        Log.d("cargar_imagenIt", "Errooooooor en la base")
    }


}



    fun modificarFechaDeVisita(indiceActual: Int, fechaNueva: LocalDate) {
        Log.i("guardar_lugar", "$indiceActual")
        val lugarActual = itinerarioActual.value.lugares?.get(indiceActual)
        lugarActual?.fechaDeVisita = fechaNueva.toString()

        Log.i("guardar_lugar", "$fechaNueva")
        // Actualizamos fecha en firebase
        try {

            val firestore = FirebaseFirestore.getInstance()
            val auth = FirebaseAuth.getInstance()

            Log.i("guardar_lugaru", "Guardando datos...")

            val documento_lugar = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
                .collection("Itinerario").document(itinerarioActual.value.id.toString()).collection("Lugar").document(indiceActual.toString())

            documento_lugar.update("fechaDeVisita", fechaNueva.toString()).addOnSuccessListener{

                Log.i("guardar_lugar", "gjhghghkgh")
                cargarLugar()

            }
                .addOnFailureListener {

                    Log.i("guardar_lugar", "Datos no guardados")
                }

        }catch(e: Exception){
            Log.i("guardar_lugar", "Error al conectar con la base de datosmsfnfsks")
        }
}

fun modificarHoraDeVisita(indiceActual: Int, horaNueva: Hora) {
    val lugarActual = _itinerarioActual.value.lugares?.get(indiceActual)
    Log.d("modificar_hora", "entroo a la función")
    val hora_string = "${horaNueva.hora}:${horaNueva.minuto}"
    lugarActual?.horaDeVisita = hora_string

    Log.d("modificar_hora", "entroo a la funciónjnjnj: $horaNueva ")

    // Actualizamos fecha en firebase
    try {

        val firestore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        Log.i("modificar_hora", "Guardando datos...")

        val documento_lugar = firestore.collection("Usuario").document(auth.currentUser?.email.toString())
            .collection("Itinerario").document(itinerarioActual.value.id.toString()).collection("Lugar").document(indiceActual.toString())

        documento_lugar.update("horaDeVisita", hora_string).addOnSuccessListener{

            Log.i("guardar_lugar", "gjhghghkgh")
            cargarLugar()

        }
            .addOnFailureListener {

                Log.i("guardar_lugar", "Datos no guardados")
            }

    }catch(e: Exception){
        Log.i("guardar_lugar", "Error al conectar con la base de datosmsfnfsks")
    }


}

fun borrarLugarActual(lugar: Lugar) {
    _itinerarioActual.value.lugares?.remove(lugar)

}




//--------------------------------------------------------------------------------------------//
//-------------------------------------API----------------------------------------------------//
//--------------------------------------------------------------------------------------------//

//Obtener preview de los lugares incluyendo el nombre, direccion e ID es ApiService.kt
//Funcion para obtener detalles del lugar con base en un ID de lugar----------------------------
fun obtenerLugar(
    placesClient: PlacesClient,
    placeId: String,
    nombre: (String?) -> Unit,
    direccion: (String?) -> Unit,
    rating: (Double?) -> Unit,
    numeroTelefono: (String?) -> Unit,
    paginaWeb: (String?) -> Unit,
    latLng: (LatLng?) -> Unit,
){
    val placeFields = listOf(
        Place.Field.NAME,
        Place.Field.ADDRESS,
        Place.Field.RATING,
        Place.Field.PHONE_NUMBER,
        Place.Field.WEBSITE_URI,
        Place.Field.LAT_LNG,
        Place.Field.PHOTO_METADATAS
    )//campos que se deben obtener de la API de places
    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place


            nombre(place.name)
            direccion(place.address)
            rating(place.rating)
            numeroTelefono(place.phoneNumber)
            latLng(place.latLng)

            if(place.websiteUri != null){
                paginaWeb(place.websiteUri?.toString())
            }

            // Obtener metadatos de la foto-----------------------------------------------------
            val metada = place.photoMetadatas
            if (metada != null) {

                val photoMetadata = metada.first()

                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(500) // Optional.
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                        val image = fetchPhotoResponse.bitmap
                        val imagenBitmap: ImageBitmap = image.asImageBitmap()
                        _imagen.value = imagenBitmap
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }
            }

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}
//Funci[on para obtener detalles para lugares cercanos------------------------------------------

fun obtenerFotoLugarCercano(
    placesClient: PlacesClient,
    placesId: List<String>,
){
    val placeFields = listOf(
        Place.Field.PHOTO_METADATAS
    )//campos que se deben obtener de la API de places



    placesId.forEachIndexed{ index, placeId ->

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                // Obtener metadatos de la foto-----------------------------------------------------
                val metada = place.photoMetadatas
                if (metada != null) {

                    val photoMetadata = metada.first()

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(300) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build()
                    placesClient.fetchPhoto(photoRequest)
                        .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                            val image = fetchPhotoResponse.bitmap
                            val imagenBitmap: ImageBitmap = image.asImageBitmap()
                            // Asegúrate de que la lista tenga un tamaño suficiente

                            while (_imagenes.value.size <= index) {
                                _imagenes.value.add(null)
                            }

                            // Agregar la imagen a la lista
                            _imagenes.value[index] = imagenBitmap

                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }
}

//funci[on para obtener informacion para el mapa------------------------------------------------
fun obtenerMarcador(
    placesClient: PlacesClient,
    placeId: String,
){
    val placeFields = listOf(
        Place.Field.ID,
        Place.Field.LAT_LNG,
        Place.Field.NAME,
        Place.Field.RATING,
        Place.Field.PHOTO_METADATAS
    )//campos que se deben obtener de la API de places

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place

            setUbicacion(place.latLng?:LatLng(ubicacion.value.latitude, ubicacion.value.longitude))
            setNombreLugar(place.name?:"")
            setRatingLugar(place.rating?:-1.0)
            setIdLugar(place.id?:"")

            val metada = place.photoMetadatas
            if (metada != null) {

                val photoMetadata = metada.first()

                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(200) // Optional.
                    .setMaxHeight(200) // Optional.
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                        val image = fetchPhotoResponse.bitmap
                        val imagenBitmap: ImageBitmap = image.asImageBitmap()
                        _imagen.value = imagenBitmap
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("testLugar", "Place not found: " + exception.message)
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }
            }

            setMarcadorInicializado(true)
            setInformacionInicializada(true)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("testLugar", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}

fun obtenerMarcadorEntreMuchos(
    placesClient: PlacesClient,
    placeId: String,
){
    val placeFields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.RATING,
        Place.Field.PHOTO_METADATAS
    )//campos que se deben obtener de la API de places

    val request = FetchPlaceRequest.newInstance(placeId, placeFields)

    placesClient.fetchPlace(request)
        .addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place

            setNombreLugar(place.name?:"")
            setRatingLugar(place.rating?:-1.0)
            setIdLugar(place.id?:"")

            val metada = place.photoMetadatas
            if (metada != null) {

                val photoMetadata = metada.first()

                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(200) // Optional.
                    .setMaxHeight(200) // Optional.
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->

                        val image = fetchPhotoResponse.bitmap
                        val imagenBitmap: ImageBitmap = image.asImageBitmap()
                        _imagen.value = imagenBitmap
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }
            }

            setInformacionInicializada(true)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("testLugar", "Place not found: ${exception.message}")
                val statusCode = exception.statusCode
                TODO("Handle error with given status code")
            }
        }
}
//--------------------------------------------------------------------------------------------//
//-------------------------------------FIREBASE-----------------------------------------------//
//--------------------------------------------------------------------------------------------//

// Itinerario funciones
private val _listaItinerario = MutableStateFlow(emptyList<Itinerario>())
val listaItinerario = _listaItinerario.asStateFlow()

fun setlistaItinerario(nuevaLista: MutableList<Itinerario>){
    _listaItinerario.value = nuevaLista
}



private val _usuario = MutableStateFlow(usuarioPrueba)
val usuario = _usuario.asStateFlow()
fun setUsuario(nuevoUsuario: Usuario) {
    _usuario.value = nuevoUsuario
}


// Funciones auxiliares
fun obtenerDato() {
    viewModelScope.launch{

        try{

            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()

            val documento =  firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await()
            val documento_itinerario = firestore.collection("Usuario").document(auth.currentUser?.email.toString()).collection("Itinerario").get().await()

            //Log.d("TTTT", firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await().getString("nombre").toString())
            //firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().result.id
            /*val documento =  firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await()
            val itinerarios_lista = mutableListOf<Itinerario>()

            val itinerarioData = documento["itinerarios"] as? List<*>

            itinerarioData?.forEach{item ->

                if(item is Map<*,*>){
                    Log.d("Itinerario_datos", "es Map")
                    val id = item["id"] as? Int
                    val nombre = item["nombre"] as? String
                    val autor = item["autor"] as? String



                    val Itinerario = Itinerario(id, nombre.orEmpty(), autor.orEmpty(), null)
                    itinerarios_lista.add(Itinerario)

                }
            }

            for(datos in itinerarios_lista){

                Log.d("Itinerario_datos", "$datos")
            }*/


            val usuario = Usuario(
                    nombre = documento.getString("nombre").toString(),
                    foto_perfil = documento.getString("foto_perfil").toString(),
                    fechaDeRegistro = documento.getString("fechaDeRegistro").toString(),
                    descripcion = documento.getString("descripcion").toString(),
                    lugarDeOrigen = documento.getString("lugarDeOrigen").toString(),
                    comentarios = null,
                    itinerarios = mutableListOf()
            )

            setUsuario(usuario)

        }catch(e: Exception){

            Log.d("Itinerario_datos", "No se cargaron los datos correctamente")
            setUsuario(usuarioPrueba)
        }
    }
}


// Logica para obtener itinerarios

private val _listaIt = MutableStateFlow(mutableListOf<Itinerario>())
val listaIt = _listaIt.asStateFlow()
fun setLista(nuevoLista: MutableList<Itinerario>) {
    _listaIt.value = nuevoLista
}

fun obtenerItinerario(){

    viewModelScope.launch{
        val firestore = FirebaseFirestore.getInstance()
        // Creamos instancias para firebase
        val auth = FirebaseAuth.getInstance()

        val lista_Itinerario: MutableList<Itinerario> = mutableListOf()

        Log.i("obtener_itinerario", "Guardando datos...")
        try{
            val documentos = firestore.collection("Usuario").document(auth.currentUser?.email.toString()).collection("Itinerario").get().await()

            if(!documentos.isEmpty || documentos != null){
                for (documento in documentos){

                    val nombre = documento.getString("nombre").toString()
                    //val temporal = documento.toObject(Itinerario::class.java)

                    val itinerario = Itinerario(
                        id = documento.getString("id").toString(),
                        nombre = documento.getString("nombre").toString(),
                        autor = documento.getString("autor").toString(),
                        lugares = null,
                        imagen = documento.getString("imagen").toString()
                    )

                    lista_Itinerario.add(itinerario)

                }

            }

            Log.i("obtener_itinerario", "-ekdkkedde ---------------------------------")
            Log.i("obtener_itinerario", "$lista_Itinerario")

            setLista(lista_Itinerario)

        }catch (e: java.lang.Exception){

            Log.i("obtener_itinerario", "Error al entrar a la base: $e")
        }

        cargarLugar()
    }

}

// Lógica para obtener lugares
// Función para obtener Lugares

private val _listaLugares = MutableStateFlow(mutableListOf<Lugar>())
val listaLugares = _listaLugares.asStateFlow()
fun setListaLugar(nuevoLista: MutableList<Lugar>) {
    _listaLugares.value = nuevoLista
}
fun cargarLugar(){

    viewModelScope.launch{
        val firestore = FirebaseFirestore.getInstance()
        // Creamos instancias para firebase
        val auth = FirebaseAuth.getInstance()

        val lista_Lugar: MutableList<Lugar> = mutableListOf()

        Log.i("obtener_lugar", "Guardando datos...")

        val id = itinerarioActual.value.id.toString()

        try{
            val documentos = firestore.collection("Usuario").document(auth.currentUser?.email.toString()).collection("Itinerario")
                .document(id).collection("Lugar").get().await()

            if(!documentos.isEmpty || documentos != null){
                for (documento in documentos){

                    val lugar = Lugar(
                        id = documento.getString("id").toString(),
                        nombreLugar = documento.getString("nombreLugar").toString(),
                        fechaDeVisita = documento.getString("fechaDeVisita").toString(),
                        horaDeVisita = documento.getString("horaDeVisita").toString(),
                        imagen = documento.getString("imagen").toString()
                    )

                    //lista_Itinerario.add(itinerario)
                    lista_Lugar.add(lugar)

                }

            }

            setListaLugar(lista_Lugar)
            Log.d("obtener_lugar", "----------------------------------------------")
            Log.d("obtener_lugar", itinerarioActual.value.id.toString())
            Log.d("obtener_lugar", "Completado")
            Log.d("obtener_lugar", "----------------------------------------------")

        }catch (e: java.lang.Exception){

            Log.i("obtener_lugar", "Error al entrar a la base: $e")
        }
    }
}



}

