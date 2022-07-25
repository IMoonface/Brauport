package com.example.brauportv2.data

import androidx.room.*
import com.example.brauportv2.model.StockItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * from stock_database ORDER BY stockName ASC")
    fun getAllStockItems(): Flow<List<StockItemData>>

    @Query("SELECT * from stock_database WHERE id = :id")
    fun getStockItem(id: Int): Flow<StockItemData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stockItem: StockItemData)

    @Update
    suspend fun update(stockItem: StockItemData)

    @Delete
    suspend fun delete(stockItem: StockItemData)
}