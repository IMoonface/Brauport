package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType

object Recipe {
    var recipeItem = RecipeItem(
        1,
        "",
        emptyList<RStockItem>().toMutableList(),
        Rest("", ""),
        emptyList<Hopping>().toMutableList(),
        RStockItem("", StockItemType.YEAST.ordinal, ""),
        MainBrew("", "")
    )

    var recipeStartConfig = RecipeItem(
        1,
        "",
        emptyList<RStockItem>().toMutableList(),
        Rest("", ""),
        emptyList<Hopping>().toMutableList(),
        RStockItem("", StockItemType.YEAST.ordinal, ""),
        MainBrew("", "")
    )
}