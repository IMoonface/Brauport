package com.example.brauportv2.mapper

import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItemData
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.recipe.RecipeItemData
import com.example.brauportv2.model.recipe.SNoAmount
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemData

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}

fun StockItem.toSNoAmount(): SNoAmount {
    return SNoAmount(id, stockName, itemType)
}

fun RecipeItemData.toRecipeItem(): RecipeItem {
    return RecipeItem(
        rId, recipeName,
        maltList.toMutableList(),
        restList.toMutableList(),
        hoppingList.toMutableList(),
        yeast,
        mainBrew
    )
}

fun RecipeItem.toRecipeItemData(): RecipeItemData {
    return RecipeItemData(rId, recipeName, maltList, restList, hoppingList, yeast, mainBrew)
}

fun RecipeItem.toBrewHistoryItem(): BrewHistoryItem {
    return BrewHistoryItem(
        rId,
        recipeName,
        maltList,
        restList,
        hoppingList,
        yeast,
        mainBrew,
        "",
        ""
    )
}

fun BrewHistoryItemData.toBrewHistoryItem(): BrewHistoryItem {
    return BrewHistoryItem(
        bId,
        bName,
        bMaltList,
        bRestList,
        bHoppingList,
        bYeast,
        bMainBrew,
        bDateOfCompletion,
        bEndOfFermentation
    )
}

fun BrewHistoryItem.toBrewHistoryItemData(): BrewHistoryItemData {
    return BrewHistoryItemData(
        bId,
        bName,
        bMaltList,
        bRestList,
        bHoppingList,
        bYeast,
        bMainBrew,
        bDateOfCompletion,
        bEndOfFermentation
    )
}

fun BrewHistoryItem.toRecipeItem(): RecipeItem {
    return RecipeItem(
        bId,
        bName,
        bMaltList.toMutableList(),
        bRestList.toMutableList(),
        bHoppingList.toMutableList(),
        bYeast,
        bMainBrew,
    )
}