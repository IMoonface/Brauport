package com.example.brauportv2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauportv2.model.StockItemType

@Entity(tableName = "stock_database")
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
