package edu.northeastern.jetpackcomposev1.models.resume

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

@Serializable
class ResumeModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    val fileName: String = "",
    val filePath: String = "",
    val time: String = ZonedDateTime.now().toString(), // auto also
) {
    var nickName: String by mutableStateOf("")
    var count: Int by mutableIntStateOf(0) // count how many applications use this resume
}
