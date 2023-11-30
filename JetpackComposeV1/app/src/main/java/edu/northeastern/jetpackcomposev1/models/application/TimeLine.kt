package edu.northeastern.jetpackcomposev1.models.Application

import kotlinx.serialization.Serializable

@Serializable
data class TimeLine (
    var results: List<Event> = emptyList(),
    var count: Int = 0
)