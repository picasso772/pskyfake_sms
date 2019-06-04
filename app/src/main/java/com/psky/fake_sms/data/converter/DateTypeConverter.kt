package com.psky.fake_sms.data.converter

import android.arch.persistence.room.TypeConverter
import java.util.*

class DateTypeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateTimestamp(date: Date?): Long? {
        return date?.time
    }
}