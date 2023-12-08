package edu.northeastern.jetpackcomposev1.models.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.Serializable

@Serializable
class SearchModel(
    val app_id: String = "f849217d",
    val app_key: String = "bd6b77c6a91d653f01015217390221f2"
) {
    var country by mutableStateOf(CountryCode.US.code)
    var page by mutableIntStateOf(1) // go to which page, start from 1
    var results_per_page by mutableIntStateOf(10)
    var what by mutableStateOf("")
    var what_and by mutableStateOf("")
    var what_phrase by mutableStateOf("")
    var what_or by mutableStateOf("")
    var what_exclude by mutableStateOf("")
    var title_only by mutableStateOf("")
    var where by mutableStateOf("")
    var distance by mutableIntStateOf(5) // km
    var max_days_old by mutableIntStateOf(365)
    var sort_dir by mutableStateOf(SortDirection.DESCENDING.code) // this field is abandoned by the api
    var sort_by by mutableStateOf(SortByCode.RELEVANCE.code)
    var salary_min by mutableIntStateOf(0)
    var salary_max by mutableIntStateOf(0)
    var salary_include_unknown by mutableStateOf(false)
    var full_time by mutableStateOf(false) // full time only
    var part_time by mutableStateOf(false) // part time only
    var contract by mutableStateOf(false) // contract job only
    var permanent by mutableStateOf(false) // permanent job only
    var company by mutableStateOf("")
}
