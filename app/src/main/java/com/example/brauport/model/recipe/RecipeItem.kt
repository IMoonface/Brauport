package com.example.brauport.model.recipe

import com.example.brauport.model.stock.StockItem

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var maltList: MutableList<StockItem>,
    var restList: MutableList<Rest>,
    var hoppingList: MutableList<Hopping>,
    var yeast: StockItem,
    var mainBrew: MainBrew
)
