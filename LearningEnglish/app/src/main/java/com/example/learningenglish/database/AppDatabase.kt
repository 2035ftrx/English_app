package com.example.learningenglish.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        User::class,
        WordEntity::class,
        WordBookEntity::class,
        WordStudyRecord::class,
        StudyTimeRecord::class,
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun wordDao(): WordDao
    abstract fun wordBookDao(): WordBookDao
    abstract fun reciteRecordDao(): WordStudyRecordDao
    abstract fun studyTimeRecordDao(): StudyTimeRecordDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app_database"
                    ).build()
                }
                return INSTANCE!!
            }
        }
    }
}
