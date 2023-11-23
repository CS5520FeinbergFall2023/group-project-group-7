package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable

@Serializable
data class JobLocationModel(
    val area: List<String> = emptyList(),
    val display_name: String = ""
)
