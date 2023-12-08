package edu.northeastern.jetpackcomposev1.utility

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

fun convertToDate(isoString: String): String {
    val zonedDateTime: ZonedDateTime = parseDateTime(isoString)
    return zonedDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
}


fun millisToDate(millis: Long? = System.currentTimeMillis()): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date(millis ?: System.currentTimeMillis()))
}
fun Long?.changeMillisToDateString(): String {
    val date: LocalDate = this?.let {
        Instant.ofEpochMilli(it).atOffset(ZoneOffset.UTC).toLocalDate()
    } ?: LocalDate.now()
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
}