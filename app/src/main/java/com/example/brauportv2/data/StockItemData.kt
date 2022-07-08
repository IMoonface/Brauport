package com.example.brauportv2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauportv2.model.StockItemType

@Entity(tableName = "stock_database")
data class StockItemData(
    @PrimaryKey
    val id: Int,
    val itemType: StockItemType,
    val stockName: String,
    val stockAmount: String
)
