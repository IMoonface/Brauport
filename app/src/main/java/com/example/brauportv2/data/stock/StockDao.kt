package com.example.brauportv2.data.stock

import androidx.room.*
import com.example.brauportv2.model.stock.StockItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * from stock_database ORDER BY stockName ASC")
    fun getAllStockItems(): Flow<List<StockItemData>>

    @Query("SELECT * from stock_database WHERE id = :id")
    fun getStockItem(id: Int): Flow<StockItemData>

    @Insert
    suspend fun insert(item: StockItemData)

    @Update
    suspend fun update(item: StockItemData)

    @Delete
    suspend fun delete(item: StockItemData)
}