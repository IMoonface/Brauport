package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType
import java.util.*

object Recipe {
    var recipeName = ""
    var rMaltList = emptyList<RStockItem>().toMutableList()
    var rRest = Rest("", "")
    var rHoppingList = emptyList<Hopping>().toMutableList()
    var rYeast = RStockItem("", "")
    var rMainBrew = MainBrew("", "")

    val recipeItem = RecipeItem(
        UUID.randomUUID().hashCode(),
        recipeName,
        rMaltList,
        rRest,
        rHoppingList,
        rYeast,
        rMainBrew
    )
}