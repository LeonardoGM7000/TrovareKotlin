package com.example.trovare.ui.theme.Pantallas

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trovare.R
import com.example.trovare.ui.theme.Data.Usuario
import com.example.trovare.ui.theme.Data.usuarioPrueba
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerfilDataModel: ViewModel() {

    private val _dato = mutableStateOf<Usuario>(usuarioPrueba)
    val dato: State<Usuario> = _dato
    
    // Funciones auxiliares
    fun obtenerDato() {
        viewModelScope.launch{

            try{

                val auth = FirebaseAuth.getInstance()
                val firestore = FirebaseFirestore.getInstance()

                //Log.d("TTTT", firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await().getString("nombre").toString())
                //firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().result.id
                val documento =  firestore.collection("Usuario").document(auth.currentUser?.email.toString()).get().await()
                val usuario = Usuario(
                    documento.getString("nombre").toString(),
                    R.drawable.perfil,
                    documento.getString("fechaDeRegistro").toString(),
                    documento.getString("descripcion").toString(),
                    documento.getString("lugarDeOrigen").toString(),
                    null
                )

                _dato.value = usuario

            }catch(e: Exception){

                _dato.value = usuarioPrueba

            }
        }

    }


}