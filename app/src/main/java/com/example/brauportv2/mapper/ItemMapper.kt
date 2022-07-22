package com.example.brauportv2.mapper

import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemData
import com.example.brauportv2.model.recipeModel.*

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}

fun RecipeItemData.toRecipeItem(): RecipeItem {
    return RecipeItem(
        rId,
        recipeName,
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

fun StockItem.toRStockItem(): RStockItem {
    return RStockItem(stockName, itemType.ordinal, stockAmount)
}

fun StockItem.toRSNoAmount(): RSNoAmount {
    return RSNoAmount(stockName, itemType.ordinal)
}

fun RStockItem.toRSNoAmount(): RSNoAmount {
    return RSNoAmount(rStockName, rItemType)
}
