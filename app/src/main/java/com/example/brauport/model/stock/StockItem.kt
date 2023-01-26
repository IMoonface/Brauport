package com.example.brauport.model.stock

data class StockItem(
    val id: Int,
    val itemType: Int,
    var name: String,
    var amount: String
)