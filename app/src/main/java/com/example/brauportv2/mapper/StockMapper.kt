package com.example.brauportv2.mapper

import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemData
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.model.recipeModel.RecipeItemData

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}

fun RecipeItem.toRecipeItemData(): RecipeItemData {
    return RecipeItemData(id, recipeName, rMaltList, rRest, rHoppingList, rYeast, rMainBrew)
}