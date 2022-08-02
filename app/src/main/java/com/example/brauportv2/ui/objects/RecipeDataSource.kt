package com.example.brauportv2.ui.objects

import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType
import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.recipe.Rest

object RecipeDataSource {
    var update = false
    val startMaltList = emptyList<StockItem>().toMutableList()
    val startRestList = emptyList<Rest>().toMutableList()
    val startHoppingList = emptyList<Hopping>().toMutableList()
    val startYeast = StockItem(1, StockItemType.YEAST.ordinal, "", "")
    val startMainBrew = MainBrew("", "")

    var recipeItem = RecipeItem(
        1,
        "",
        emptyList<StockItem>().toMutableList(),
        emptyList<Rest>().toMutableList(),
        emptyList<Hopping>().toMutableList(),
        StockItem(1, StockItemType.YEAST.ordinal, "", ""),
        MainBrew("", ""),
    )

    var recipeItemList = emptyList<RecipeItem>().toMutableList()
}