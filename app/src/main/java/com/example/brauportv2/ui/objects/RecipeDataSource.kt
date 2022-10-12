package com.example.brauportv2.ui.objects

import com.example.brauportv2.model.brew.StepItem
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.YEAST

object RecipeDataSource {

    var recipeItem = RecipeItem(
        0,
        "",
        mutableListOf(),
        mutableListOf(),
        mutableListOf(),
        StockItem(0, YEAST.ordinal, "", ""),
        MainBrew("", "")
    )

    var spinnerItemsList = emptyList<RecipeItem>()
    var stepList = emptyList<StepItem>()
}