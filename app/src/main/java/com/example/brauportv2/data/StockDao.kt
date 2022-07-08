package com.example.brauportv2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.brauportv2.model.StockItem
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * from stock_database ORDER BY stockName ASC")
    fun getAllStockItems(): Flow<List<StockItemData>>

    @Query("SELECT * from stock_database WHERE id = :id")
    fun getStockItem(id: Long): Flow<StockItemData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockItem: StockItemData)

    @Update
    suspend fun update(stockItem: StockItemData)

    @Delete
    suspend fun delete(stockItem: StockItemData)
}