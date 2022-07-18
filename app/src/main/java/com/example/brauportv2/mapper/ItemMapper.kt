package com.example.brauportv2.mapper

import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemData
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.model.recipeModel.RecipeItemData

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}

fun RecipeItemData.toRecipeItem(): RecipeItem {
    return RecipeItem(rId, recipeName, rMaltList.toMutableList(), rRest, rHoppingList.toMutableList(), rYeast, rMainBrew)
}

fun RecipeItem.toRecipeItemData(): RecipeItemData {
    return RecipeItemData(rId, recipeName, rMaltList, rRest, rHoppingList, rYeast, rMainBrew)
}

fun StockItem.toRStockItem(): RStockItem {
    return RStockItem(stockName, itemType.ordinal, stockAmount)
}

fun Hopping.toRStockItem(): RStockItem {
    return RStockItem(rHoppingName, rItemType, rHoppingAmount)
}