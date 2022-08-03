package com.example.brauportv2.ui.objects

import com.example.brauportv2.adapter.RecipeAdapter
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem

object TextWatcherLogic {

    var startTimer = false

    fun filterListForStock(text: String, adapter: StockAdapter, stockList: List<StockItem>) {
        if (text != "" && text.endsWith("g")) {
            adapter.submitList(stockList.filter {
                it.stockAmount.removeSuffix("g").toInt() <=
                        text.removeSuffix("g").toInt()
            })
        } else if (text != "")
            adapter.submitList(
                stockList.filter {
                    it.stockName.lowercase().contains(text.lowercase())
                }
            )
        else
            adapter.submitList(stockList)
    }

    fun filterListForRecipe(text: String, adapter: RecipeAdapter, recipeList: List<RecipeItem>) {
        if (text != "")
            adapter.submitList(recipeList.filter {
                it.recipeName.contains(text)
            })
        else
            adapter.submitList(recipeList)
    }
}