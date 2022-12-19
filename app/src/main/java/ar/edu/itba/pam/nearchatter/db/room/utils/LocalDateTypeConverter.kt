package ar.edu.itba.pam.nearchatter.db.room.utils

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

class LocalDateTimeTypeConverter {

    @TypeConverter
    fun toTimestamp(localDate: LocalDateTime): Long {
        return localDate.toEpochSecond(ZoneOffset.UTC)
    }

    @TypeConverter
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
    }
}
