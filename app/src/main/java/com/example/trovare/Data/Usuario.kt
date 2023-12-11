package com.example.trovare.Data

data class Usuario(
    val nombre: String,
    //@DrawableRes val foto_perfil: Int = R.drawable.perfil,
    val foto_perfil: String?,
    val fechaDeRegistro: String,
    val descripcion: String?,
    val lugarDeOrigen: String?,
    val comentarios: List<String>?,
    val itinerarios: MutableList<Itinerario>,
    val favoritos: MutableList<LugarFavorito>
)

//val usuarioPrueba = Usuario(nombre = "Usuario Prueba", fechaDeRegistro = "2023", descripcion = "Descripcion del usuario de prueba Descripcion del usuario de prueba",  lugarDeOrigen = "Mexico", comentarios = listOf("primera reseña del usuario primera reseña del usuario", "segunda reseña del usuario", "tercera reseña del usuario"))
val usuarioPrueba = Usuario(
    nombre = "",
    foto_perfil = null,
    fechaDeRegistro = "2023",
    descripcion = "",
    lugarDeOrigen = "",
    comentarios = null,
    itinerarios = mutableListOf(itinerarioPrueba),
    favoritos = mutableListOf(lugarFavoritoPrueba),
)