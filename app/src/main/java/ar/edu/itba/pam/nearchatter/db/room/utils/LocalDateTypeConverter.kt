package ar.edu.itba.pam.nearchatter.db.room.utils

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTypeConverter {

    @TypeConverter
    fun toTimestamp(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(timestamp: Long): LocalDate {
        return LocalDate.ofEpochDay(timestamp)
    }
}
