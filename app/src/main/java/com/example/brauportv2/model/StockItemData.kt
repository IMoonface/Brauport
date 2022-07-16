package com.example.brauportv2.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "stock_database", indices = [Index(value = ["stockName", "itemType"], unique = true)])
data class StockItemData(
    @PrimaryKey
    val id: Int,
    @ColumnInfo (name = "itemType")
    val itemType: StockItemType,
    @ColumnInfo (name = "stockName")
    val stockName: String,
    @ColumnInfo (name = "stockAmount")
    val stockAmount: String
)
