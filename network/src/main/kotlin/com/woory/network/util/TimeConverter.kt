package com.woory.network.util

import org.threeten.bp.Instant
import org.threeten.bp.OffsetDateTime

fun OffsetDateTime.asMillis() = toInstant().toEpochMilli()

fun Long.asOffsetDateTime(): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(this), Constants.zoneId)