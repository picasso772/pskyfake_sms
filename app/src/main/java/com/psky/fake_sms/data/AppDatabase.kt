package com.psky.fake_sms.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.psky.fake_sms.data.converter.DateTypeConverter
import com.psky.fake_sms.data.dao.UserDao
import com.psky.fake_sms.data.model.User

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        var instance: AppDatabase? = null

        fun getAppdatabase(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance =
                        Room.databaseBuilder(context, AppDatabase::class.java, "fakeSMS")
                            .fallbackToDestructiveMigration()
                            .build()
                }
            }
            return instance
        }

        fun destroyDatabase() {
            instance = null
        }
    }
}