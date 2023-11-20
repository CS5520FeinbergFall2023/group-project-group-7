package edu.northeastern.jetpackcomposev1.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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

    enum class CountryCode(val fullName: String, val code: String) {
        GB("United Kingdom", "gb"),
        US("United States", "us"),
        CA("Canada", "ca")
    }
    enum class SortDirection(val code: String) {
        ASCENDING("up"),
        DESCENDING("down")
    }
    enum class SortBy(val code: String) {
        RELEVANCE("relevance"),
        DATE("date"),
        SALARY("salary")
    }



}

//class Job() {
//    val job_id: String
//    val title: String
//    val company: String
//}