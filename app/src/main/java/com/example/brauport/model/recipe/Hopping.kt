package com.example.brauport.model.recipe

import com.example.brauport.model.stock.StockItem

data class Hopping(
    var hopList: MutableList<StockItem>,
    var hoppingTime: String
)