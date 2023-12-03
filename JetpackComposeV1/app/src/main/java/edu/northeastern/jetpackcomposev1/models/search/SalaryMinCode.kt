package edu.northeastern.jetpackcomposev1.models.search

enum class SalaryMinCode(val displayName: String, val code: Int) {
    ALL("All", 0),
    MIN50("$50,000", 50000),
    MIN75("$75,000", 75000),
    MIN100("$100,000", 100000),
    MIN125("$125,000", 125000)
}