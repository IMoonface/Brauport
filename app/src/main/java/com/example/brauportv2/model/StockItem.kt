package com.example.brauportv2.model

data class StockItem(
    val id: Int,
    val itemType: StockItemType,
    val name: String,
    val amount: String
)