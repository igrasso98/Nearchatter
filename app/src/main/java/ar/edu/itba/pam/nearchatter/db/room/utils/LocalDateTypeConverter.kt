package ar.edu.itba.pam.nearchatter.db.room.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateTypeConverter {

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toTimestamp(localDate: LocalDate): Long {
        return localDate.toEpochDay()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocalDate(timestamp: Long): LocalDate {
//        return LocalDate.ofEpochDay(timestamp)
//         Use this to test
        return LocalDate.now()
    }
}
