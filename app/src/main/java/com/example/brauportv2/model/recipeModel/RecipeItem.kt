package com.example.brauportv2.model.recipeModel

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var rMaltList: MutableList<Malt>,
    var rRest: Rest,
    var rHoppingList: MutableList<Hopping>,
    var rYeast: Yeast,
    var rMainBrew: MainBrew
)
