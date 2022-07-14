package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType
import java.util.*

object Recipe {
    var rId = UUID.randomUUID().hashCode()
    var recipeName = "Test Rezept"
    var rMaltList = emptyList<Malt>().toMutableList()
    var rRest = Rest("", "")
    var rHoppingList = emptyList<Hopping>().toMutableList()
    var rYeast = Yeast("", StockItemType.YEAST.ordinal, "")
    var rMainBrew = MainBrew("", "")

    val recipeItem = RecipeItem(
        rId,
        recipeName,
        rMaltList,
        rRest,
        rHoppingList,
        rYeast,
        rMainBrew
    )
}