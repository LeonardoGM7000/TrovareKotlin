package com.example.trovare.ui.theme.Navegacion

import androidx.lifecycle.ViewModel
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
 * [OrderViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */

class TrovareViewModel : ViewModel() {



    private val _estadoUi = MutableStateFlow(ConfiguracionEstadoUi())
    val uiState: StateFlow<ConfiguracionEstadoUi> = _estadoUi.asStateFlow()

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

    /*

    /**
     * Set the [desiredFlavor] of cupcakes for this order's state.
     * Only 1 flavor can be selected for the whole order.
     */
    fun setFlavor(desiredFlavor: String) {
        _EstadoUi.update { currentState ->
            currentState.copy(flavor = desiredFlavor)
        }
    }

    /**
     * Set the [pickupDate] for this order's state and update the price
     */
    fun setDate(pickupDate: String) {
        _EstadoUi.update { currentState ->
            currentState.copy(
                date = pickupDate,
                price = calculatePrice(pickupDate = pickupDate)
            )
        }
    }

    /**
     * Reset the order state
     */
    fun resetOrder() {
        _EstadoUi.value = ConfiguracionEstadoUi(pickupOptions = pickupOptions())
    }

    /**
     * Returns the calculated price based on the order details.
     */
    private fun calculatePrice(
        quantity: Int = _EstadoUi.value.quantity,
        pickupDate: String = _EstadoUi.value.date
    ): String {
        var calculatedPrice = quantity * PRICE_PER_CUPCAKE
        // If the user selected the first option (today) for pickup, add the surcharge
        if (pickupOptions()[0] == pickupDate) {
            calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
        }
        val formattedPrice = NumberFormat.getCurrencyInstance().format(calculatedPrice)
        return formattedPrice
    }

    /**
     * Returns a list of date options starting with the current date and the following 3 dates.
     */
    private fun pickupOptions(): List<String> {
        val dateOptions = mutableListOf<String>()
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())
        val calendar = Calendar.getInstance()
        // add current date and the following 3 dates.
        repeat(4) {
            dateOptions.add(formatter.format(calendar.time))
            calendar.add(Calendar.DATE, 1)
        }
        return dateOptions
    }

     */


}