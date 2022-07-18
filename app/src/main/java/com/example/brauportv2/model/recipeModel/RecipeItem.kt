package com.example.brauportv2.model.recipeModel

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var maltList: MutableList<RStockItem>,
    var restList: MutableList<Rest>,
    val hoppingList: MutableList<Hopping>,
    var yeast: RStockItem,
    var mainBrew: MainBrew
)
