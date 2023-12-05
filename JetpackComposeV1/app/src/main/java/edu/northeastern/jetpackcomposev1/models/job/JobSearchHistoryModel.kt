package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

@Serializable
data class JobSearchHistoryModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    val country: String = "us", // default
    val what: String = "",
    val company: String = "",
    val where: String = "",
    val distance: Int = 5, // default
    val time: String = ZonedDateTime.now().toString() // auto get one, no need to input
)
