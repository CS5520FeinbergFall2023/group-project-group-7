package edu.northeastern.jetpackcomposev1.utility

import edu.northeastern.jetpackcomposev1.models.job.JobSearchResultModel
import edu.northeastern.jetpackcomposev1.models.search.ContractTimeCode
import edu.northeastern.jetpackcomposev1.models.search.SalaryMinCode
import edu.northeastern.jetpackcomposev1.models.search.SortByCode
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun urlEncoding(originalString: String): String {
    return URLEncoder.encode(originalString, StandardCharsets.UTF_8.toString())
}

fun findSortByCode(sort_by: String): SortByCode? {
    return SortByCode.values().firstOrNull { it.code == sort_by }
}

fun findContractTimeCode(full_time: Boolean, part_time: Boolean): ContractTimeCode? {
    return ContractTimeCode.values().firstOrNull { it.fullTime == full_time && it.partTime == part_time }
}

fun findSalaryMinCode(salary_min: Int): SalaryMinCode? {
    return SalaryMinCode.values().firstOrNull { it.code == salary_min }
}