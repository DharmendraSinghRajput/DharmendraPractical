package com.ssti.dharmendrapractical.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ssti.dharmendrapractical.data.model.CartsResponse
import com.ssti.dharmendrapractical.utils.Converters

@Database(
    entities = [ProfileEntity::class,CartsResponse::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}
