package com.example.brauportv2.model.recipeModel

import com.example.brauportv2.model.StockItemType
import java.util.*

object Recipe {
    var recipeItem = RecipeItem(
        1,
        "",
        emptyList<Malt>().toMutableList(),
        Rest("", ""),
        emptyList<Hopping>().toMutableList(),
        Yeast("", StockItemType.YEAST.ordinal, ""),
        MainBrew("", "")
    )

    var recipeStartConfig = RecipeItem(
        1,
        "",
        emptyList<Malt>().toMutableList(),
        Rest("", ""),
        emptyList<Hopping>().toMutableList(),
        Yeast("", StockItemType.YEAST.ordinal, ""),
        MainBrew("", "")
    )
}