package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable

@Serializable
data class JobModel(
    val location: JobLocationModel = JobLocationModel(),
    val redirect_url: String = "",
    val salary_is_predicted: String = "",
    val description: String = "",
    val longitude: Double = 0.0,
    val title: String = "",
    val id: String = "",
    val created: String = "",
    val company: JobCompanyModel = JobCompanyModel(),
    val contract_time: String = "full_time",
    val category: JobCategoryModel = JobCategoryModel(),
    val contract_type: String = "",
    val latitude: Double = 0.0,
    val salary_max: Double = 0.0,
    val salary_min: Double = 0.0,
)
