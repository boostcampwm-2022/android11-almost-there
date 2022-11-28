package com.woory.database.util

import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import java.util.*

object Constants {
    val zoneId: ZoneId = ZoneId.of("Asia/Seoul")
    val zoneOffset: ZoneOffset = ZoneOffset.of("+09:00")

    val locale: Locale = Locale.KOREA
}