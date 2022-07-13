package com.example.brauportv2.model.recipeModel

data class RecipeItem(
    val id: Int,
    val recipeName: String,
    val rMaltList: List<RStockItem>,
    val rRest: Rest,
    val rHoppingList: List<Hopping>,
    val rYeast: RStockItem,
    val rMainBrew: MainBrew
)
