package edu.northeastern.jetpackcomposev1.utility

import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/**
 * convert json string to any data type
 */
fun <T> fromJson(jsonString: String, serializer: KSerializer<T>): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(serializer, jsonString)
}

/**
 * convert any data type to a json string
 */
fun <T : Any> toJson(data: T, serializer: KSerializer<T>): String {
    return Json.encodeToString(ListSerializer(serializer), listOf(data))
}

fun decodeDummySearchResultJson(): JobSearchResultModel {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(dummyJson)
}