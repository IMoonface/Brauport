package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType

object RecipeDataSource {

    var update = false

    val startMaltList = emptyList<RStockItem>().toMutableList()
    val startRestList = emptyList<Rest>().toMutableList()
    val startHoppingList = emptyList<Hopping>().toMutableList()
    val startYeast = RStockItem("", StockItemType.YEAST.ordinal, "")
    val startMainBrew = MainBrew("", "")

    var recipeItem = RecipeItem(
        1,
        "",
        emptyList<RStockItem>().toMutableList(),
        emptyList<Rest>().toMutableList(),
        emptyList<Hopping>().toMutableList(),
        RStockItem("", StockItemType.YEAST.ordinal, ""),
        MainBrew("", "")
    )

    var recipeItemList = emptyList<RecipeItem>().toMutableList()
}