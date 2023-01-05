package com.example.brauport.ui.`object`

import com.example.brauport.adapter.RecipeAdapter
import com.example.brauport.adapter.StockAdapter
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem

object TextWatcherLogic {

    fun filterListForStock(text: String, adapter: StockAdapter, list: List<StockItem>) {
        if (text != "" && isNumeric(text)) {
            adapter.submitList(list.filter { it.stockAmount.toInt() <= text.toInt() })
        } else if (text != "")
            adapter.submitList(list.filter { it.stockName.lowercase().contains(text.lowercase()) })
        else
            adapter.submitList(list)
    }

    fun filterListForRecipe(text: String, adapter: RecipeAdapter, list: List<RecipeItem>) {
        if (text != "")
            adapter.submitList(list.filter { it.recipeName.lowercase().contains(text.lowercase()) })
        else
            adapter.submitList(list)
    }

    private fun isNumeric(s: String): Boolean {
        return try {
            s.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}