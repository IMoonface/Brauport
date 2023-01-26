package com.example.brauport.data.stock

import androidx.room.*
import com.example.brauport.model.stock.StockItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * from stock_database ORDER BY name ASC")
    fun getAllStockItems(): Flow<List<StockItemData>>

    @Insert
    suspend fun insert(item: StockItemData)

    @Update
    suspend fun update(item: StockItemData)

    @Delete
    suspend fun delete(item: StockItemData)
}