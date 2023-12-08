package edu.northeastern.jetpackcomposev1.models.job

/**
 * You can modify this to fit your design, add or change or delete some
 */
enum class ApplicationStatus(val displayName: String) {
    APPLIED("Applied"),
    INTERVIEWED("Interviewed"),
    REJECTED("Rejected"),
    OFFER("Offer"),
    OFFER_ACCEPTED("Offer Accepted")
}