package com.example.brauportv2.mapper

import com.example.brauportv2.data.StockItemData
import com.example.brauportv2.model.StockItem

fun StockItemData.toStockItem(): StockItem {
    return StockItem(id, itemType, stockName, stockAmount)
}

fun StockItem.toStockItemData(): StockItemData {
    return StockItemData(id, itemType, stockName, stockAmount)
}