package com.example.trovare.ui.theme.Navegacion

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.trovare.ui.theme.Data.Lugar
import com.example.trovare.ui.theme.Data.LugarAutocompletar
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [TrovareViewModel] guarda información de la aplicación dentro del ciclo de vida.
 */

class TrovareViewModel : ViewModel() {



    private val _estadoUi = MutableStateFlow(TrovareEstadoUi())
    val uiState: StateFlow<TrovareEstadoUi> = _estadoUi.asStateFlow()
    //Pantalla-configuración------------------------------------------------------------------------
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

    //Pantalla-soporte------------------------------------------------------------------------------

    fun setResultoUtil(nuevaSeleccion: String){
        _estadoUi.update { estadoActual ->
            estadoActual.copy(
                resultoUtil = nuevaSeleccion
            )
        }
    }

    //Función para autocompletar lugares en la barra de busqueda------------------------------------
    fun autocompletar(
        placesClient: PlacesClient,
        query: String,
        listaLugares: MutableList<LugarAutocompletar>
    ){

        val token = AutocompleteSessionToken.newInstance()

        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder()

                .setCountries("MX")
                .setTypesFilter(listOf(PlaceTypes.ADDRESS))
                .setSessionToken(token)
                .setQuery(query)
                .build()

        //Limpiar resultados encontrados previamente
        listaLugares.clear()

        //Buscar predicciones para autocompletar la busqueda
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                for (prediction in response.autocompletePredictions) {
                    val idLugar = prediction.placeId
                    val textoPrimario = prediction.getPrimaryText(null).toString()
                    val textoSecundario = prediction.getSecondaryText(null).toString()
                    Log.i("maps api", textoPrimario)
                    listaLugares.add(LugarAutocompletar(id = idLugar, textoPrimario = textoPrimario, textoSecundario = textoSecundario))
                }
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("maps api", "no se encontro el lugar: ${exception.statusCode}")
                }
            }
    }

    //Funcion para obtener detalles del lugar con base en un ID de lugar----------------------------
    fun obtenerLugar(
        placesClient: PlacesClient,
        placeId: String,
        lugar: Lugar
    ){

        val placeFields = listOf(Place.Field.NAME, Place.Field.RATING, Place.Field.ADDRESS, Place.Field.OPENING_HOURS, Place.Field.WEBSITE_URI, /*Place.Field.LAT_LNG*/)//campos que se deben obtener de la API de places
        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                lugar.setNombre(place.name)
                lugar.setCalificacion(place.rating)
                lugar.setDireccion(place.address)
                lugar.setHorario(place.openingHours)
                lugar.setPagina(place.websiteUri)

                Log.i("testLugar", "Place found: ${place.name}")
            }.addOnFailureListener { exception: Exception ->
                if (exception is ApiException) {
                    Log.e("testLugar", "Place not found: ${exception.message}")
                    val statusCode = exception.statusCode
                    TODO("Handle error with given status code")
                }
            }
    }


}