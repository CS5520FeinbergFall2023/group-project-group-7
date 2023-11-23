package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable

@Serializable
data class JobSearchResultModel(
    val mean: Double = 0.0,
    val results: List<JobModel> = emptyList(),
    val count: Int = 0
)

