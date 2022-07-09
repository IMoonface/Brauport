package com.example.brauportv2.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauportv2.model.StockItemType

@Entity(tableName = "stock_database")
data class StockItemData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val itemType: StockItemType,
    val stockName: String,
    val stockAmount: String
)
