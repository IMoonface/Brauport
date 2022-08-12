package com.example.brauportv2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.stock.StockDao
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItemData
import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BrewViewModel(private val stockDao: StockDao) : ViewModel() {

    var changeInStock = false

    val allStockItems: Flow<List<StockItemData>> = stockDao.getAllStockItems()

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

    fun minutes(millis: Long): String {
        if (millis / 60000 < 1) return "00"
        if (millis / 60000 in 1..9) return "0" + (millis / 60000)
        return "" + (millis / 60000)
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

    fun calcAmount(item: StockItem, list: List<StockItem>): String {
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val databaseAmount = list[index].stockAmount.substringBefore("g").toInt()
        return (databaseAmount - recipeAmount).toString() + "g"
    }

    private fun calcForShortage(item: StockItem, list: List<StockItem>): Boolean {
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val index = list.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        if (index == -1) {
            changeInStock = true
            return false
        }
        val databaseAmount = list[index].stockAmount.substringBefore("g").toInt()
        return databaseAmount - recipeAmount >= 0
    }

    fun proveForNonNegAmount(item: RecipeItem, list: List<StockItem>): Boolean {
        var possible = true
        item.maltList.forEach { malt ->
            if (!calcForShortage(malt, list))
                possible = false
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                if (!calcForShortage(hop, list))
                    possible = false
            }
        }

        if (!calcForShortage(item.yeast, list))
            possible = false

        return possible
    }

    fun createStringList(item: RecipeItem): List<BrewItem> {
        val newBrewList = mutableListOf<BrewItem>()

        item.maltList.forEach {
            newBrewList.add(
                BrewItem(it.stockName + " " + it.stockAmount, "", false)
            )
        }

        newBrewList.add(BrewItem("Malz Schroten", "", false))

        newBrewList.add(
            BrewItem("Hauptguss: " + item.mainBrew.firstBrew, "", false)
        )

        item.restList.forEach {
            newBrewList.add(BrewItem(it.restTemp, it.restTime, false))
        }

        newBrewList.add(
            BrewItem("Nachguss: " + item.mainBrew.secondBrew, "", false)
        )

        newBrewList.add(BrewItem("Malz entnehmen", "", false))
        newBrewList.add(BrewItem("Auf etwa Temperatur erhitzen", "", false))

        var hoppingListString = ""
        item.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                hoppingListString += hop.stockName + " " + hop.stockAmount + " "
            }
            newBrewList.add(BrewItem(hoppingListString, hopping.hoppingTime, false))
            hoppingListString = ""
        }

        newBrewList.add(BrewItem("Schlauchen", "", false))
        newBrewList.add(BrewItem("Abkühlen lassen", "", false))

        newBrewList.add(
            BrewItem(
                item.yeast.stockName + " " + item.yeast.stockAmount,
                "",
                false
            )
        )

        return newBrewList
    }
}

class BrewViewModelFactory(private val stockDao: StockDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrewViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BrewViewModel(stockDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}