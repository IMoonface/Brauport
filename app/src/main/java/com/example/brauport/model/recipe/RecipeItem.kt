package com.example.brauport.model.recipe

import com.example.brauport.model.stock.StockItem

data class RecipeItem(
    var id: Int,
    var name: String,
    var maltList: MutableList<StockItem>,
    var restList: MutableList<Rest>,
    var hoppingList: MutableList<Hopping>,
    var yeast: StockItem,
    var mainBrew: MainBrew,
    val dateOfCompletion: String,
    val endOfFermentation: String,
    var cardColor: Int,
    var isBrewHistoryItem: Boolean,
    var isRecipeItem: Boolean
)
