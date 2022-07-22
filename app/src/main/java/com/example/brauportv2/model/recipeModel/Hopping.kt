package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItem

data class Hopping(
    var hopsList: MutableList<StockItem>,
    var hoppingTime: String
)