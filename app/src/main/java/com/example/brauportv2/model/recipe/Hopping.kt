package com.example.brauportv2.model.recipe

import com.example.brauportv2.model.stock.StockItem

data class Hopping(
    var hopList: MutableList<StockItem>,
    var hoppingTime: String
)