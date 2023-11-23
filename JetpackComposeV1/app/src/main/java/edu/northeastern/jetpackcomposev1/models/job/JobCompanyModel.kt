package edu.northeastern.jetpackcomposev1.models.job

import kotlinx.serialization.Serializable

/**
 * Naming should match the json from the external job api
 */
@Serializable
data class JobCompanyModel(
    val display_name: String = ""
)