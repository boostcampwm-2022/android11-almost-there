package com.woory.almostthere.presentation.util

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.Calendar
import java.util.Locale

object TimeConverter {

    private val zoneId: ZoneId = ZoneId.of("Asia/Seoul")
    val zoneOffset: ZoneOffset = ZoneOffset.of("+09:00")
    private val locale: Locale = Locale.KOREA

    fun LocalDate.asCalendar(): Calendar {
        val calendar = Calendar.getInstance(locale)
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
        OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), zoneId)
}