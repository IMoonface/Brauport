package com.example.brauportv2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.stock.StockDao
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItemData
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StockViewModel(private val stockDao: StockDao) : ViewModel() {

    var changeInStock = false

    val allStockItems: Flow<List<StockItemData>> = stockDao.getAllStockItems()

    fun addStock(stockItem: StockItem) {
        viewModelScope.launch {
            stockDao.insert(stockItem.toStockItemData())
        }
    }

    fun updateStock(id: Int, itemType: Int, stockName: String, stockAmount: String) {
        val stockItem = StockItem(
            id = id, itemType = itemType, stockName = stockName, stockAmount = stockAmount
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

    fun proveForNonNegAmount(item: RecipeItem, list: List<StockItem>): Boolean {
        changeInStock = false

        item.maltList.forEach { malt ->
            if (!calcForShortage(malt, list))
                return false
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                if (!calcForShortage(hop, list))
                    return false
            }
        }

        if (!calcForShortage(item.yeast, list))
            return false

        return true
    }

    private fun calcForShortage(
        item: StockItem,
        list: List<StockItem>,
    ): Boolean {
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        if (index == -1) {
            changeInStock = true
            return false
        }

        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val databaseAmount = list[index].stockAmount.substringBefore("g").toInt()
        return databaseAmount - recipeAmount >= 0
    }

    fun calcAmount(item: StockItem, list: List<StockItem>): String {
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val databaseAmount = list[index].stockAmount.substringBefore("g").toInt()
        return (databaseAmount - recipeAmount).toString() + "g"
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