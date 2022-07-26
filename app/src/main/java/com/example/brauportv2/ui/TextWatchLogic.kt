package com.example.brauportv2.ui

import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.model.StockItem

object TextWatchLogic {
    fun filterListForKeyword(text: String, adapter: StockAdapter, stockList: List<StockItem>) {
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
}