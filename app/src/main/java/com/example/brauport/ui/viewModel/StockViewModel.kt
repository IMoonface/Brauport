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

    fun addStock(item: StockItem) {
        viewModelScope.launch {
            stockDao.insert(item.toStockItemData())
        }
    }

    fun updateStock(id: Int, itemType: Int, name: String, amount: String) {
        val item = StockItem(
            id = id, itemType = itemType, name = name, amount = amount
        )
        viewModelScope.launch(Dispatchers.IO) {
            stockDao.update(item.toStockItemData())
        }
    }

    fun deleteStock(item: StockItem) {
        viewModelScope.launch(Dispatchers.IO) {
            stockDao.delete(item.toStockItemData())
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
        val recipeAmount = item.amount.toInt()
        val databaseAmount = list[index].amount.toInt()
        return databaseAmount - recipeAmount >= 0
    }

    private fun calcAmount(item: StockItem, list: List<StockItem>): String {
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val recipeAmount = item.amount.toInt()
        val databaseAmount = list[index].amount.toInt()
        return (databaseAmount - recipeAmount).toString()
    }

    fun updateDatabase(item: RecipeItem, list: List<StockItem>) {
        item.maltList.forEach { malt ->
            updateStock(
                id = malt.id,
                itemType = malt.itemType,
                name = malt.name,
                amount = calcAmount(malt, list)
            )
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                updateStock(
                    id = hop.id,
                    itemType = hop.itemType,
                    name = hop.name,
                    amount = calcAmount(hop, list)
                )
            }
        }

        updateStock(
            id = item.yeast.id,
            itemType = item.yeast.itemType,
            name = item.yeast.name,
            amount = calcAmount(item.yeast, list)
        )
    }

    fun minutes(millis: Long): String {
        if (millis / 60000 < 1) return "00:"
        if (millis / 60000 in 1..9) return "0" + (millis / 60000) + ":"
        return "" + (millis / 60000) + ":"
    }

    fun seconds(millis: Long): String {
        var millisSeconds: Long = millis
        while (millisSeconds >= 60000)
            millisSeconds -= 60000

        return when (millisSeconds / 1000) {
            in 0..0 -> "00"
            in 1..9 -> "0" + +(millisSeconds / 1000)
            else -> "" + millisSeconds / 1000
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