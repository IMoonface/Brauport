package com.example.brauportv2.model

data class StockItem(
    var id: Int,
    var itemType: StockItemType,
    var stockName: String,
    var stockAmount: String
)