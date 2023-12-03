package edu.northeastern.jetpackcomposev1.models.search

enum class SortByCode(val displayName: String, val code: String) {
    RELEVANCE("Relevance", "relevance"),
    DATE("Date", "date"),
    SALARY("Salary", "salary")
}