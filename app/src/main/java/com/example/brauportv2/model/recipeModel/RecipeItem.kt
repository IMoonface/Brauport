package com.example.brauportv2.model.recipeModel

data class RecipeItem(
    val rId: Int,
    val recipeName: String,
    val rMaltList: List<Malt>,
    val rRest: Rest,
    val rHoppingList: List<Hopping>,
    val rYeast: Yeast,
    val rMainBrew: MainBrew
)
