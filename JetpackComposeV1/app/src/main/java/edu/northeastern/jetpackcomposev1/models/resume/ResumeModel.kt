package edu.northeastern.jetpackcomposev1.models.resume

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable
import edu.northeastern.jetpackcomposev1.utility.getCurrentZonedDateTime
import java.util.UUID

@Serializable
class ResumeModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    var fileName: String = "", // name of the pdf file
    var filePath: String = "", // storage path
    val time: String = getCurrentZonedDateTime(), // auto also
    var activeStatus: Boolean = true // if delete the resume； it turns to false
) {
    var nickName: String by mutableStateOf("") // label
    var count: Int by mutableIntStateOf(0) // count how many applications use this resume
}
