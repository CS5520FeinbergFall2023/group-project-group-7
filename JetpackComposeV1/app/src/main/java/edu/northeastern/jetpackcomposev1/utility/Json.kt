package edu.northeastern.jetpackcomposev1.utility

import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun decodeDummySearchResultJson(): JobSearchResultModel {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(dummyJson)
}