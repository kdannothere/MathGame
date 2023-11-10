package com.kdannothere.mathgame.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.data.record.RecordDao

@Database(
    entities = [Record::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun getRecordDao(): RecordDao

    companion object {
        private const val dbName = "app_database"
        const val tableRecords = "records"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    dbName
                )
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }

}
