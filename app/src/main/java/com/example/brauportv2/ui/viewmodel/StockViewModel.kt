package com.example.brauportv2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.StockDao
import com.example.brauportv2.model.StockItemData
import com.example.brauportv2.mapper.toStockItemData
import com.example.brauportv2.model.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StockViewModel(private val stockDao: StockDao) : ViewModel() {
    val allStockItems: Flow<List<StockItemData>> = stockDao.getAllStockItems()

    fun addStock(stockItem: StockItem) {
        viewModelScope.launch {
            stockDao.insert(stockItem.toStockItemData())
        }
    }

    fun updateStock(
        id: Int,
        itemType: Int,
        stockName: String,
        stockAmount: String
    ) {
        val stockItem = StockItem(
            id = id,
            itemType = itemType,
            stockName = stockName,
            stockAmount = stockAmount
        )
        viewModelScope.launch(Dispatchers.IO) {
            stockDao.update(stockItem.toStockItemData())
        }
    }

    fun deleteStock(stockItem: StockItem) {
        viewModelScope.launch(Dispatchers.IO) {
            stockDao.delete(stockItem.toStockItemData())
        }
    }
}

class StockViewModelFactory(private val stockDao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockViewModel(stockDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}