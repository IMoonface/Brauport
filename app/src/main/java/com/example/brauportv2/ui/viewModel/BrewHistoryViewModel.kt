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
import java.text.ParseException
import java.text.SimpleDateFormat

class BrewHistoryViewModel(private val brewHistoryDao: BrewHistoryDao) : ViewModel() {

    val allBrewHistoryItems: Flow<List<BrewHistoryItemData>> =
        brewHistoryDao.getAllBrewHistoryItems()

    fun addBrewHistoryItem(brewHistoryItem: BrewHistoryItem) {
        viewModelScope.launch {
            brewHistoryDao.insert(brewHistoryItem.toBrewHistoryItemData())
        }
    }

    fun updateBrewFinished(id: Int, brewFinished: Boolean) {
        viewModelScope.launch {
            brewHistoryDao.updateBrewFinished(id, brewFinished)
        }
    }

    fun updateBrewHistoryItem(
        bId: Int,
        bName: String,
        bMaltList: List<StockItem>,
        bRestList: List<Rest>,
        bHoppingList: List<Hopping>,
        bYeast: StockItem,
        bMainBrew: MainBrew,
        bDateOfCompletion: String,
        bEndOfFermentation: String,
        cardColor: Int,
        brewFinished: Boolean
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
            bEndOfFermentation = bEndOfFermentation,
            cardColor = cardColor,
            brewFinished = brewFinished
        )
        viewModelScope.launch(Dispatchers.IO) {
            brewHistoryDao.update(brewHistoryItem.toBrewHistoryItemData())
        }
    }

    fun deleteBrewHistoryItem(brewHistoryItem: BrewHistoryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            brewHistoryDao.delete(brewHistoryItem.toBrewHistoryItemData())
        }
    }

    fun dateIsValid(endOfFermentation: String, formatter: SimpleDateFormat): Boolean {
        try {
            formatter.parse(endOfFermentation)
        } catch (e: ParseException) {
            return false
        }
        return true
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