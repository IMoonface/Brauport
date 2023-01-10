package com.example.brauport.mapper

import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.recipe.RecipeItemData
import com.example.brauport.model.stock.SNoAmount
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemData

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
        id = id,
        name = name,
        maltList = maltList.toMutableList(),
        restList = restList.toMutableList(),
        hoppingList = hoppingList.toMutableList(),
        yeast = yeast,
        mainBrew = mainBrew,
        dateOfCompletion = dateOfCompletion,
        endOfFermentation = endOfFermentation,
        cardColor = cardColor,
        isBrewHistoryItem = isBrewHistoryItem,
        isRecipeItem = isRecipeItem
    )
}

fun RecipeItem.toRecipeItemData(): RecipeItemData {
    return RecipeItemData(
        id = id,
        name = name,
        maltList = maltList,
        restList = restList,
        hoppingList = hoppingList,
        yeast = yeast,
        mainBrew = mainBrew,
        dateOfCompletion = dateOfCompletion,
        endOfFermentation = endOfFermentation,
        cardColor = cardColor,
        isBrewHistoryItem = isBrewHistoryItem,
        isRecipeItem = isRecipeItem
    )
}
