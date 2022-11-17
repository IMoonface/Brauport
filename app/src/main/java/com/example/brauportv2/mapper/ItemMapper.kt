package com.example.brauportv2.mapper

import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItemData
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.recipe.RecipeItemData
import com.example.brauportv2.model.stock.SNoAmount
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemData

fun StockItemData.toStockItem(): StockItem {
    return StockItem(
        id = id,
        itemType = itemType,
        stockName = stockName,
        stockAmount = stockAmount
    )
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(
        id = id,
        itemType = itemType,
        stockName = stockName,
        stockAmount = stockAmount
    )
}

fun StockItem.toSNoAmount(): SNoAmount {
    return SNoAmount(
        id = id,
        itemType = itemType,
        stockName = stockName
    )
}

fun RecipeItemData.toRecipeItem(): RecipeItem {
    return RecipeItem(
        rId = rId,
        recipeName = recipeName,
        maltList = maltList.toMutableList(),
        restList = restList.toMutableList(),
        hoppingList = hoppingList.toMutableList(),
        yeast = yeast,
        mainBrew = mainBrew
    )
}

fun RecipeItem.toRecipeItemData(): RecipeItemData {
    return RecipeItemData(
        rId = rId,
        recipeName = recipeName,
        maltList = maltList,
        restList = restList,
        hoppingList = hoppingList,
        yeast = yeast,
        mainBrew = mainBrew
    )
}

fun RecipeItem.toBrewHistoryItem(): BrewHistoryItem {
    return BrewHistoryItem(
        bId = rId,
        bName = recipeName,
        bMaltList = maltList,
        bRestList = restList,
        bHoppingList = hoppingList,
        bYeast = yeast,
        bMainBrew = mainBrew,
        bDateOfCompletion = "",
        bEndOfFermentation = "",
        cardColor = 1
    )
}

fun BrewHistoryItemData.toBrewHistoryItem(): BrewHistoryItem {
    return BrewHistoryItem(
        bId = bId,
        bName = bName,
        bMaltList = bMaltList,
        bRestList = bRestList,
        bHoppingList = bHoppingList,
        bYeast = bYeast,
        bMainBrew = bMainBrew,
        bDateOfCompletion = bDateOfCompletion,
        bEndOfFermentation = bEndOfFermentation,
        cardColor = cardColor
    )
}

fun BrewHistoryItem.toBrewHistoryItemData(): BrewHistoryItemData {
    return BrewHistoryItemData(
        bId = bId,
        bName = bName,
        bMaltList = bMaltList,
        bRestList = bRestList,
        bHoppingList = bHoppingList,
        bYeast = bYeast,
        bMainBrew = bMainBrew,
        bDateOfCompletion = bDateOfCompletion,
        bEndOfFermentation = bEndOfFermentation,
        cardColor = cardColor
    )
}

fun BrewHistoryItem.toRecipeItem(): RecipeItem {
    return RecipeItem(
        rId = bId,
        recipeName = bName,
        maltList = bMaltList.toMutableList(),
        restList = bRestList.toMutableList(),
        hoppingList = bHoppingList.toMutableList(),
        yeast = bYeast,
        mainBrew = bMainBrew,
    )
}