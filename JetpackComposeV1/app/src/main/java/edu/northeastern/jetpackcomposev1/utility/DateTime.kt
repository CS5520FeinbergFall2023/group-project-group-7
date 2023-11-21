package edu.northeastern.jetpackcomposev1.utility

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun parseDateTime(isoString: String): ZonedDateTime {
    // val isoString = "2023-11-15T12:03:47Z" this is the format
    return ZonedDateTime.parse(isoString)
}

fun parseDate(isoString: String): LocalDate {
    return parseDateTime(isoString).toLocalDate()
}

fun parseTime(isoString: String): LocalTime {
    return parseDateTime(isoString).toLocalTime()
}

fun convertDateTime(created: String): String {
    val createdDateTime: ZonedDateTime = parseDateTime(created)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now()
    val duration = Duration.between(createdDateTime, currentDateTime)
    val days = duration.toDays()
    return if(days == 0L) "Posted today" else "Posted $days days ago"
}