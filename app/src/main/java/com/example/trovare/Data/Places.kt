package com.example.trovare.Data

import com.google.gson.annotations.SerializedName

data class LugarAutocompletar(
    val id: String,
    val textoPrimario: String,
    val textoSecundario: String
)

data class PlacesClass (
    @SerializedName("places" ) var places : List<Places?> = arrayListOf()
)

data class Places (
    @SerializedName("id" ) var id : String,
    @SerializedName("formattedAddress" ) var formattedAddress : String?,
    @SerializedName("displayName" ) var displayName : DisplayName?,
)

data class DisplayName(
    @SerializedName("text" ) var text : String?,
    @SerializedName("languageCode" ) var languageCode : String?
)

//Lugares cercanos----------------------------------------------------------------------------------
data class PlacesNearbyClass (
    @SerializedName("places" ) var placesNearby : List<PlacesNearby?> = arrayListOf()
)

data class PlacesNearby (
    @SerializedName("id" ) var id : String,
    //@SerializedName("displayName" ) var displayName : DisplayName?,
)



