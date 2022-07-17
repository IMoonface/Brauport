package com.example.brauportv2.model.recipeModel

data class RecipeItem(
    var rId: Int,
    var recipeName: String,
    var rMaltList: MutableList<RStockItem>,
    var rRest: Rest,
    var rHopsList: MutableList<RStockItem>,
    var rTimeList: MutableList<String>,
    var rYeast: RStockItem,
    var rMainBrew: MainBrew
)
