package com.example.brauportv2.model.recipeModel

data class Hopping(
    var hopsList: MutableList<RStockItem>,
    var hoppingTime: String
)