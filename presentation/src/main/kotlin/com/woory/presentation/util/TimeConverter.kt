package com.woory.presentation.util

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import java.util.*

fun LocalDate.asCalendar(): Calendar {
    val calendar = Calendar.getInstance(Constants.locale)
    calendar.set(year, monthValue - 1, dayOfMonth)
    return calendar
}

fun Calendar.asLocalDate(): LocalDate {
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH)
    val dayOfMonth = get(Calendar.DAY_OF_MONTH)
    return LocalDate.of(year, month + 1, dayOfMonth)
}

fun OffsetDateTime.asMillis() = toInstant().toEpochMilli()

fun Long.asOffsetDateTime(): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), Constants.zoneId)