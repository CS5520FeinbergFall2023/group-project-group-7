package edu.northeastern.jetpackcomposev1.models.post

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.UUID

class PostModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
) {
    var user_id: String = ""
    var user_name: String by mutableStateOf("")
    var avatar_filePath: String by mutableStateOf("")
    var text: String by mutableStateOf("")
    var images: List<PostImageModel> by mutableStateOf(emptyList())
    var likes: List<String> by mutableStateOf(emptyList()) // this will store user id
    //var comments: List<PostCommentModel> by mutableStateOf(emptyList())
    var url: String by mutableStateOf("")
    var time: String = ""
}
