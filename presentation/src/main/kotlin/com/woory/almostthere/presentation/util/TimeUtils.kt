package com.woory.almostthere.presentation.util

import android.content.Context
import androidx.annotation.StringRes
import com.woory.almostthere.presentation.R
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

object TimeUtils {

    fun getDurationStringInMinuteToDay(
        context: Context,
        startDateTime: OffsetDateTime,
        endDateTime: OffsetDateTime
    ): String {
        val duration = Duration.between(startDateTime, endDateTime)

        val days = duration.toDaysPart()
        val hours = duration.toHoursPart()
        val minutes = duration.toMinutesPart()

        val daysWithSuffix = getTimeStringWithSuffix(context, DateTimeType.DAY, days.toInt())
        val hoursWithSuffix = getTimeStringWithSuffix(context, DateTimeType.HOUR, hours)
        val minutesWithSuffix = getTimeStringWithSuffix(context, DateTimeType.MINUTE, minutes)

        return if (days > 0L) {
            daysWithSuffix
        } else if (hours > 0) {
            listOf(hoursWithSuffix, minutesWithSuffix).filterNot { it.isEmpty() }.joinToString(" ")
        } else {
            minutesWithSuffix
        }
    }

    private fun getTimeStringWithSuffix(
        context: Context,
        dateTimeType: DateTimeType,
        value: Int
    ): String =
        if (value > 0) {
            dateTimeType.withSuffix(context, value)
        } else {
            if (dateTimeType == DateTimeType.MINUTE && value == 0) {
                dateTimeType.withSuffix(context, value)
            } else {
                ""
            }
        }

    fun getStringInMinuteToDay(
        context: Context,
        time: Int
    ): String {
        val minute = time % 60
        val hour = (time / 60) % 24
        val day = (time / (60 * 24))

        val sb = java.lang.StringBuilder()
        if (day > 0) sb.append(getTimeStringWithSuffix(context, DateTimeType.DAY, day))
        if (hour > 0) sb.append(getTimeStringWithSuffix(context, DateTimeType.HOUR, hour))
        if (minute > 0) sb.append(getTimeStringWithSuffix(context, DateTimeType.MINUTE, minute))

        return sb.toString()
    }

    enum class DateTimeType(@StringRes private val withSuffixResId: Int) {
        YEAR(R.string.years_with_suffix),
        MONTH(R.string.months_with_suffix),
        DAY(R.string.days_with_suffix),
        HOUR(R.string.hours_with_suffix),
        MINUTE(R.string.minutes_with_suffix),
        SECOND(R.string.seconds_with_suffix);

        fun withSuffix(context: Context, value: Int): String =
            context.getString(withSuffixResId, value)
    }

    fun getOffsetDateTimeToFormatString(offsetDateTime: OffsetDateTime): String {
        return offsetDateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd a hh:mm"))
    }
}