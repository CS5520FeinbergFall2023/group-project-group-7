package edu.northeastern.jetpackcomposev1.models.search

enum class SortBy(val displayName: String, val code: String) {
    HYBRID("Hybrid", "hybrid"),
    DATE("Date", "date"),
    SALARY("Salary", "salary"),
    RELEVANCE("Relevance", "relevance")
}