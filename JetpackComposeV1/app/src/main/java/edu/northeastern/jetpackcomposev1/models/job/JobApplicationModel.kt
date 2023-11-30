package edu.northeastern.jetpackcomposev1.models.job

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.northeastern.jetpackcomposev1.models.Application.TimeLine
import edu.northeastern.jetpackcomposev1.models.resume.ResumeModel
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID

@Serializable
class JobApplicationModel(
    val id: String = UUID.randomUUID().toString(), // save your life, auto generate one, no need to get one from the firebase
    val job: JobModel = JobModel(),
) {
    var resume: ResumeModel by mutableStateOf(ResumeModel())
    //timeLine in the detail page
    var timeLine: TimeLine by mutableStateOf(TimeLine())
}
