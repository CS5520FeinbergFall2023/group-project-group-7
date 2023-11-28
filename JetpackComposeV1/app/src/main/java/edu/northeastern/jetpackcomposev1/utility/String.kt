package edu.northeastern.jetpackcomposev1.utility

import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun urlEncoding(originalString: String): String {
    return URLEncoder.encode(originalString, StandardCharsets.UTF_8.toString())
}