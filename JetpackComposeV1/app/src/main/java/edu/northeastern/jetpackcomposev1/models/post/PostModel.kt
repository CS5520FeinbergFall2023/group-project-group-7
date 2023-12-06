package edu.northeastern.jetpackcomposev1.models.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

@Serializable
class PostModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    val user_id: String,
    val time: String = ZonedDateTime.now().toString() // auto get one, no need to input
) {
    var text: String by mutableStateOf("")
    var images: List<PostImageModel> = emptyList()
}
