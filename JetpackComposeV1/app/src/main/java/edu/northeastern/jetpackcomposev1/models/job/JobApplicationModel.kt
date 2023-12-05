package edu.northeastern.jetpackcomposev1.models.job

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import edu.northeastern.jetpackcomposev1.models.application.TimeLine
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
    var date: String by mutableStateOf(ZonedDateTime.now().toString()) // default applied time
    var status: String by mutableStateOf(ApplicationStatus.APPLIED.displayName) // default status
    var timeLine: TimeLine by mutableStateOf(TimeLine())
}
