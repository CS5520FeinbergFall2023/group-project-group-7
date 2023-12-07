package edu.northeastern.jetpackcomposev1.models.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

//@Serializable
class PostModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
) {
    var user_id: String = ""
    var user_name: String by mutableStateOf("")
    var avatar_filePath: String by mutableStateOf("")
    var text: String by mutableStateOf("")
    var images: List<PostImageModel> = emptyList()
    var likes: List<String> = emptyList() // this will store user id
    var comments: List<CommentModel> = emptyList()
    var time: String = ""
}
