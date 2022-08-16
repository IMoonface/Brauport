package com.example.brauportv2.data.brewHistory

import androidx.room.*
import com.example.brauportv2.model.brewHistory.BrewHistoryItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface BrewHistoryDao {
    @Query("SELECT * from brew_history_database ORDER BY brewHistoryItemName ASC")
    fun getAllBrewHistoryItems(): Flow<List<BrewHistoryItemData>>

    @Query("SELECT * from brew_history_database WHERE bId = :id")
    fun getBrewHistoryItem(id: Int): Flow<BrewHistoryItemData>

    @Insert
    suspend fun insert(item: BrewHistoryItemData)

    @Update
    suspend fun update(item: BrewHistoryItemData)

    @Delete
    suspend fun delete(item: BrewHistoryItemData)
}