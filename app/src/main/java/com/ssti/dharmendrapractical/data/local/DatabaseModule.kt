package com.ssti.dharmendrapractical.data.hilt

import android.content.Context
import androidx.room.Room
import com.ssti.dharmendrapractical.data.local.AppDatabase
import com.ssti.dharmendrapractical.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        )
        .fallbackToDestructiveMigration() // Use only in dev - removes old data on migration
        .build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()
}
