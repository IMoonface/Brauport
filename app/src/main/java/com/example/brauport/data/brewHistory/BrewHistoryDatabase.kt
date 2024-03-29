package com.example.brauport.data.brewHistory

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.brauport.data.RoomConverters
import com.example.brauport.model.brewHistory.BrewHistoryItemData

@Database(entities = [BrewHistoryItemData::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class BrewHistoryDatabase : RoomDatabase() {
    abstract fun brewHistoryDao(): BrewHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: BrewHistoryDatabase? = null

        fun getDatabase(context: Context): BrewHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BrewHistoryDatabase::class.java,
                    "brew_history_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}