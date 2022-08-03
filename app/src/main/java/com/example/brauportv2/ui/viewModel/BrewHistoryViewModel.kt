package com.example.brauportv2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.brewHistory.BrewHistoryDao
import com.example.brauportv2.mapper.toBrewHistoryItemData
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItemData
import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.Rest
import com.example.brauportv2.model.stock.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BrewHistoryViewModel(private val brewHistoryDao: BrewHistoryDao) : ViewModel() {
    val allBrewHistoryItems: Flow<List<BrewHistoryItemData>> =
        brewHistoryDao.getAllBrewHistoryItems()

    fun addBrewHistoryItem(brewHistoryItem: BrewHistoryItem) {
        viewModelScope.launch {
            brewHistoryDao.insert(brewHistoryItem.toBrewHistoryItemData())
        }
    }

    fun updateBrewHistoryItem(
        bId: Int,
        bName: String,
        bMaltList: MutableList<StockItem>,
        bRestList: MutableList<Rest>,
        bHoppingList: MutableList<Hopping>,
        bYeast: StockItem,
        bMainBrew: MainBrew,
        bDateOfCompletion: String,
        bEndOfFermentation: String
    ) {
        val brewHistoryItem = BrewHistoryItem(
            bId = bId,
            bName = bName,
            bMaltList = bMaltList,
            bRestList = bRestList,
            bHoppingList = bHoppingList,
            bYeast = bYeast,
            bMainBrew = bMainBrew,
            bDateOfCompletion = bDateOfCompletion,
            bEndOfFermentation = bEndOfFermentation
        )
        viewModelScope.launch(Dispatchers.IO) {
            brewHistoryDao.update(brewHistoryItem.toBrewHistoryItemData())
        }
    }

    fun deleteRecipe(brewHistoryItem: BrewHistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            brewHistoryDao.delete(brewHistoryItem.toBrewHistoryItemData())
        }
    }
}

class BrewHistoryViewModelFactory(private val brewHistoryDao: BrewHistoryDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrewHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BrewHistoryViewModel(brewHistoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}