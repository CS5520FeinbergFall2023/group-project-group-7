package edu.northeastern.jetpackcomposev1.utility

import android.util.Log
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
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
fun dateToMillis(dateString: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd")
    format.timeZone = TimeZone.getTimeZone("UTC") // Set the desired time zone
    try {
        val date = format.parse(dateString)
        return date?.time ?: 0L
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0L
}
fun millisToDate(millis: Long): String {
    if(millis == 0L) return ""
    val format = SimpleDateFormat("yyyy-MM-dd")
    format.timeZone = TimeZone.getTimeZone("UTC")
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = millis
    return format.format(calendar.time)
}