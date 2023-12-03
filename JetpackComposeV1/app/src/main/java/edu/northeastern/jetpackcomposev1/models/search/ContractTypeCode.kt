package edu.northeastern.jetpackcomposev1.models.search

enum class ContractTypeCode(val displayName: String, val contract: Boolean, val permanent: Boolean) {
    ALL("All", false, false),
    FULL("Contract", true, false),
    PART("Permanent", false, true)
}