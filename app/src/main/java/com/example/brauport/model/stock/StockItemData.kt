package com.example.brauport.model.stock

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stock_database")
data class StockItemData(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "itemType")
    val itemType: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: String
)
