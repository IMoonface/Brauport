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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brewHistoryItem: BrewHistoryItemData)

    @Update
    suspend fun update(brewHistoryItem: BrewHistoryItemData)

    @Delete
    suspend fun delete(brewHistoryItem: BrewHistoryItemData)
}