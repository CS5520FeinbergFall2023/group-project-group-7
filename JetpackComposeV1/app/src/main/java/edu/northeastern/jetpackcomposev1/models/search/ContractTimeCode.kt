package edu.northeastern.jetpackcomposev1.models.search

enum class ContractTimeCode(val displayName: String, val fullTime: Boolean, val partTime: Boolean) {
    ALL("All", false, false),
    FULL("Full-time", true, false),
    PART("Part-time", false, true)
}