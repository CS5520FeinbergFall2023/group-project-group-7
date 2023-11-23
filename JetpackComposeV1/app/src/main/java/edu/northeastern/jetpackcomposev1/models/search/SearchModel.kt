package edu.northeastern.jetpackcomposev1.models.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SearchModel(
    val app_id: String = "f849217d",
    val app_key: String = "bd6b77c6a91d653f01015217390221f2"
) {
    var country = mutableStateOf(CountryCode.US.code)
    var page by mutableIntStateOf(1) // go to which page, start from 1
    var results_per_page = mutableIntStateOf(20)
    var what = mutableStateOf("")
    var what_and = mutableStateOf("")
    var what_phrase = mutableStateOf("")
    var what_or = mutableStateOf("")
    var what_exclude = mutableStateOf("")
    var title_only = mutableStateOf("")
    var where = mutableStateOf("")
    var distance = mutableIntStateOf(5) // km
    var max_days_old = mutableIntStateOf(365)
    var sort_dir = mutableStateOf(SortDirection.DESCENDING.code)
    var sort_by = mutableStateOf(SortBy.RELEVANCE.code)
    var salary_min = mutableIntStateOf(0)
    var salary_max = mutableIntStateOf(1000000)
    var salary_include_unknown = mutableStateOf(false)
    var full_time = mutableStateOf(false)
    var part_time = mutableStateOf(false)
    var requestURL = ""
}
