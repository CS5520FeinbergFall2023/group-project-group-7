package edu.northeastern.jetpackcomposev1.models.job

/**
 * You can modify this to fit your design, add or change or delete some
 */
enum class ApplicationStatus(val displayName: String) {
    APPLIED("Applied"),
    IN_PROGRESS("In Progress"),
    UNDER_CONSIDERATION("Under Consideration"),
    INTERVIEWING("Interviewing"),
    INTERVIEWED("Interviewed"),
    REJECTED("Rejected"),
    OFFER("Offer"),
    OFFER_ACCEPTED("Offer Accepted")
}