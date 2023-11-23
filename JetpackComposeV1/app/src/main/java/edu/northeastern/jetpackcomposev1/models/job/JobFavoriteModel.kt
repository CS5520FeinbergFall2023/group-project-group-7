package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable
data class JobFavoriteModel(
    val id: String = "", // this is the job's id, not random one
    val job: JobModel = JobModel(),
    val time: String = ZonedDateTime.now().toString() // auto get one, no need to input
)
