package edu.northeastern.jetpackcomposev1.utility

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun parseDateTime(isoString: String): ZonedDateTime {
    // val isoString = "2023-11-15T12:03:47Z" this is the format
    return ZonedDateTime.parse(isoString)
}

fun convertDateTime(created: String): String {
    val createdDateTime: ZonedDateTime = parseDateTime(created)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now()
    val duration = Duration.between(createdDateTime, currentDateTime)
    val days = duration.toDays()
    var output = ""
    output = if(days == 0L) {
        "Posted today"
    } else if(days == 1L) {
        "Posted 1 day ago"
    } else {
        "Posted $days days ago"
    }
    return output
}

fun checkIfNew(created: String): Boolean {
    val createdDateTime: ZonedDateTime = parseDateTime(created)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now()
    val duration = Duration.between(createdDateTime, currentDateTime)
    val days = duration.toDays()
    return days <= 7L
}

fun getCurrentZonedDateTime(): String {
    return ZonedDateTime.now().toString()
}
fun dateToMillis(dateString: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = format.parse(dateString)
    return date?.time ?: 0L
}
fun millisToDate(millis: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(millis)
}