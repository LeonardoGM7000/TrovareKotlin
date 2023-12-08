package com.example.trovare.Data

import com.google.gson.annotations.SerializedName


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
data class NearbyPlacesClass (
    @SerializedName("places" ) var placesNearby : List<NearbyPlaces?> = arrayListOf()
)

data class NearbyPlaces (
    @SerializedName("id" ) var id : String,
    @SerializedName("displayName" ) var displayName : DisplayName?,
    @SerializedName("shortFormattedAddress" ) var shortFormattedAddress : String?,
)

data class NearbyLocationsClass (
    @SerializedName("places" ) var nearbyLocations : List<NearbyLocations?> = arrayListOf()
)

data class NearbyLocations (
    @SerializedName("location" ) var location : Location,
    @SerializedName("id") var id: String
)

data class Location (
    @SerializedName("latitude" ) var latitude : Double,
    @SerializedName("longitude" ) var longitude : Double,
)

data class PhotoNamesClass(
    @SerializedName("photos" ) var photoNames: List<PhotoNames?>? = arrayListOf()
)

data class PhotoNames(
    @SerializedName("name") var photoName: String?
)

data class PhotoURI(
    @SerializedName("photoUri") var photoUri: String
)

data class Routes(
    @SerializedName("routes") var routes: List<InformationRoute> = arrayListOf()
)

data class InformationRoute(
    @SerializedName("distanceMeters") var distance: Float,
    @SerializedName("duration") var duration: String,
    @SerializedName("polyline") var polyline: EncodPoly
)
data class EncodPoly(
    @SerializedName("encodedPolyline") var encodPolyline: String
)


