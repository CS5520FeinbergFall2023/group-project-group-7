package edu.northeastern.jetpackcomposev1.models.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
class ProfileModel() {
    var avatar: AvatarModel by mutableStateOf(AvatarModel())
    var email: String by mutableStateOf("")
    //var password: String by mutableStateOf("") this feature is depressed
    var name: String by mutableStateOf("")
    var bio: String by mutableStateOf("")
    val date: String = ZonedDateTime.now().toString() // auto get one joined date
}
