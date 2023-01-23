package com.example.brauport.model.recipe

import com.example.brauport.model.stock.StockItem

data class RecipeItem(
    var id: Int,
    var name: String,
    val maltList: MutableList<StockItem>,
    val restList: MutableList<Rest>,
    val hoppingList: MutableList<Hopping>,
    var yeast: StockItem,
    var mainBrew: MainBrew
)
