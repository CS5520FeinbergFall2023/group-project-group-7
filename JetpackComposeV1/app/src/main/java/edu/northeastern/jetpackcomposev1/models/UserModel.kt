package edu.northeastern.jetpackcomposev1.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.northeastern.jetpackcomposev1.models.user.ProfileModel
import kotlinx.serialization.Serializable

@Serializable
class UserModel() {
    var id: String by mutableStateOf("") // get this from the firebase auth after sign in or sign up
    var profile: ProfileModel by mutableStateOf(ProfileModel())
}
