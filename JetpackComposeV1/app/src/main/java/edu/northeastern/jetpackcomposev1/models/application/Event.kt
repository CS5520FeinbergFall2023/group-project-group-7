package edu.northeastern.jetpackcomposev1.models.application


import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class Event (
    val status: String = "",
    val date: String = ZonedDateTime.now().toString(),
    )