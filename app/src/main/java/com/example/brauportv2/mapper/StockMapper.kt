package com.example.brauportv2.mapper

import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemData

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}