package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType

data class Hopping(
    val name: String,
    val itemType: Int = StockItemType.HOP.ordinal,
    val amount: String,
    val time: String
)