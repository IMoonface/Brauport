package com.example.brauportv2.model.recipe

import com.example.brauportv2.model.stock.StockItem

data class Hopping(
    var hopsList: MutableList<StockItem>,
    var hoppingTime: String
)