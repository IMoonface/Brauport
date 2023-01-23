package com.example.brauport.data.brewHistory

import androidx.room.*
import com.example.brauport.model.brewHistory.BrewHistoryItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface BrewHistoryDao {
    @Query("SELECT * from brew_history_database ORDER BY name ASC")
    fun getAllBrewHistoryItems(): Flow<List<BrewHistoryItemData>>

    @Insert
    suspend fun insert(item: BrewHistoryItemData)

    @Update
    suspend fun update(item: BrewHistoryItemData)

    @Delete
    suspend fun delete(item: BrewHistoryItemData)
}