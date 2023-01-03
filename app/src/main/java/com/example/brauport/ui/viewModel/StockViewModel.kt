package com.example.brauport.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauport.data.stock.StockDao
import com.example.brauport.mapper.toSNoAmount
import com.example.brauport.mapper.toStockItemData
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemData
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

    fun negativeAmount(item: RecipeItem, list: List<StockItem>): Boolean {
        item.maltList.forEach { malt ->
            if (!itemShortage(malt, list))
                return true
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                if (!itemShortage(hop, list))
                    return true
            }
        }

        if (!itemShortage(item.yeast, list))
            return true

        return false
    }

    private fun itemShortage(item: StockItem, list: List<StockItem>): Boolean {
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val recipeAmount = item.stockAmount.toInt()
        val databaseAmount = list[index].stockAmount.toInt()
        return databaseAmount - recipeAmount >= 0
    }

    private fun calcAmount(item: StockItem, list: List<StockItem>): String {
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val recipeAmount = item.stockAmount.toInt()
        val databaseAmount = list[index].stockAmount.toInt()
        return (databaseAmount - recipeAmount).toString()
    }

    fun updateDatabase(item: RecipeItem, list: List<StockItem>) {
        item.maltList.forEach { malt ->
            updateStock(
                id = malt.id,
                itemType = malt.itemType,
                stockName = malt.stockName,
                stockAmount = calcAmount(malt, list)
            )
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                updateStock(
                    id = hop.id,
                    itemType = hop.itemType,
                    stockName = hop.stockName,
                    stockAmount = calcAmount(hop, list)
                )
            }
        }

        updateStock(
            id = item.yeast.id,
            itemType = item.yeast.itemType,
            stockName = item.yeast.stockName,
            stockAmount = calcAmount(item.yeast, list)
        )
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