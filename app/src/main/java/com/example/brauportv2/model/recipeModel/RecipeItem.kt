package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItem

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var maltList: MutableList<StockItem>,
    var restList: MutableList<Rest>,
    var hoppingList: MutableList<Hopping>,
    var yeast: StockItem,
    var mainBrew: MainBrew,
    val dateOfCompletion: String,
    val endOfFermentation: String
)
