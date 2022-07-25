package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType

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
        "",
        ""
    )

    var recipeItemList = emptyList<RecipeItem>().toMutableList()
}