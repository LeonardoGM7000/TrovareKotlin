package com.example.trovare.Data

import androidx.compose.ui.graphics.ImageBitmap
import com.google.gson.annotations.SerializedName

//Lugares cercanos----------------------------------------------------------------------------------
data class NearbyPlacesClass (
    @SerializedName("places" ) var placesNearby : List<NearbyPlaces?> = arrayListOf()
)

data class NearbyPlaces(
    @SerializedName("id" ) var id : String,
    @SerializedName("displayName" ) var displayName : DisplayName?,
    @SerializedName("rating") var rating: Float?,
    @SerializedName("primaryType") var primaryType: String?,
    var imagen: ImageBitmap?
)

val lugarTemporal = NearbyPlaces(id = "", displayName = null, rating = null, primaryType = null, imagen = null)