package edu.northeastern.jetpackcomposev1.models.post

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

@Serializable
data class PostImageModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    val fileName: String = "",
    var filePath: String = "",
    val time: String = ZonedDateTime.now().toString() // auto get one, no need to input
)
