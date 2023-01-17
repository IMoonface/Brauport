package com.example.brauport.model.recipe

import com.example.brauport.model.stock.StockItem

data class Hopping(
    val hopList: MutableList<StockItem>,
    val hoppingTime: String
)