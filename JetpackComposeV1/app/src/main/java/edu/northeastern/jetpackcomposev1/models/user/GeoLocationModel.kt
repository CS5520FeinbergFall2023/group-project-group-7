package edu.northeastern.jetpackcomposev1.models.user

import kotlinx.serialization.Serializable

@Serializable
data class GeoLocationModel(
    val ip: String,
    val city: String,
    val region: String,
    val country: String,
    val loc: String,
    val org: String,
    val postal: String,
    val timezone: String,
    val readme: String
)
