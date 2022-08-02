package com.example.brauportv2.model.recipe

import com.example.brauportv2.model.stock.StockItem

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var maltList: MutableList<StockItem>,
    var restList: MutableList<Rest>,
    var hoppingList: MutableList<Hopping>,
    var yeast: StockItem,
    var mainBrew: MainBrew
)
