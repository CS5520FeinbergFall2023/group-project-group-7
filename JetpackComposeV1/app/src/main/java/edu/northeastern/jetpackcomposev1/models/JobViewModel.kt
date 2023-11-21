package edu.northeastern.jetpackcomposev1.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import edu.northeastern.jetpackcomposev1.utility.dummyJson
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.LocalDateTime

class JobViewModel: ViewModel() {
    var country = mutableStateOf(CountryCode.US.code)
    val app_id = "f849217d"
    val app_key = "bd6b77c6a91d653f01015217390221f2"
    var page by mutableIntStateOf(1) // go to which page, start from 1
    var results_per_page = mutableIntStateOf(20)
    var what = mutableStateOf("")
    var what_and = mutableStateOf("")
    var what_phrase = mutableStateOf("")
    var what_or = mutableStateOf("")
    var what_exclude = mutableStateOf("")
    var title_only = mutableStateOf("")
    var where = mutableStateOf("")
    var distance = mutableIntStateOf(25) // km
    var max_days_old = mutableIntStateOf(365)
    var sort_dir = mutableStateOf(SortDirection.DESCENDING.code)
    var sort_by = mutableStateOf(SortBy.RELEVANCE.code)
    var salary_min = mutableIntStateOf(0)
    var salary_max = mutableIntStateOf(1000000)
    var salary_include_unknown = mutableStateOf(false)
    var full_time = mutableStateOf(false)
    var part_time = mutableStateOf(false)
    var requestURL = ""

    var response: ApiResponse = parseJson(dummyJson)



}

/*************************************************************/
enum class CountryCode(val displayName: String, val code: String) {
    GB("United Kingdom", "gb"),
    US("United States", "us"),
    CA("Canada", "ca")
}
enum class SortDirection(val displayName: String, val code: String) {
    ASCENDING("Ascending", "up"),
    DESCENDING("Descending", "down")
}
enum class SortBy(val displayName: String, val code: String) {
    HYBRID("Hybrid", "hybrid"),
    DATE("Date", "date"),
    SALARY("Salary", "salary"),
    RELEVANCE("Relevance", "relevance")
}
/*************************************************************/
fun parseJson(jsonString: String): ApiResponse {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<ApiResponse>(jsonString)
}

@Serializable
data class ApiResponse(
    val mean: Double = 0.0,
    val results: List<ApiResult> = emptyList(),
    val count: Int = 0
)

@Serializable
data class ApiResult(
    val location: ApiLocation,
    val redirect_url: String = "",
    val salary_is_predicted: String = "",
    val description: String = "",
    val longitude: Double = 0.0,
    val title: String = "",
    val id: String = "",
    val created: String = "",
    val company: ApiCompany,
    val contract_time: String = "full_time",
    val category: ApiCategory,
    val contract_type: String = "",
    val latitude: Double = 0.0,
    val salary_max: Double = 0.0,
    val salary_min: Double = 0.0,
)

@Serializable
data class ApiLocation(
    val area: List<String> = emptyList(),
    val display_name: String = ""
)

@Serializable
data class ApiCompany(
    val display_name: String = ""
)

@Serializable
data class ApiCategory(
    val tag: String = "",
    val label: String = ""
)
/*************************************************************/
class Job(

    val job_id: String,
    val title: String,
    val company: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val time: LocalDateTime,
    val contract_time: String, // full_time or part_time
    val description: String,
    val url: String,
    val salary_is_predicted: Boolean,
    val location_display_name: String,
    val location_area: String
) {}