package com.example.brauport.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauport.data.recipe.RecipeDao
import com.example.brauport.mapper.toRecipeItemData
import com.example.brauport.model.recipe.*
import com.example.brauport.model.stock.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {

    val allRecipeItems: Flow<List<RecipeItemData>> = recipeDao.getAllRecipeItems()

    fun addRecipe(item: RecipeItem) {
        viewModelScope.launch {
            recipeDao.insert(item.toRecipeItemData())
        }
    }

    fun updateRecipe(
        id: Int,
        name: String,
        maltList: MutableList<StockItem>,
        restList: MutableList<Rest>,
        hoppingList: MutableList<Hopping>,
        yeast: StockItem,
        mainBrew: MainBrew,
        dateOfCompletion: String,
        endOfFermentation: String,
        cardColor: Int,
        isBrewHistoryItem: Boolean,
        isRecipeItem: Boolean
    ) {
        val item = RecipeItem(
            id = id,
            name = name,
            maltList = maltList,
            restList = restList,
            hoppingList = hoppingList,
            yeast = yeast,
            mainBrew = mainBrew,
            dateOfCompletion = dateOfCompletion,
            endOfFermentation = endOfFermentation,
            cardColor = cardColor,
            isBrewHistoryItem = isBrewHistoryItem,
            isRecipeItem = isRecipeItem
        )
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.update(item.toRecipeItemData())
        }
    }

    fun deleteRecipe(item: RecipeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.delete(item.toRecipeItemData())
        }
    }
}

class RecipeViewModelFactory(private val recipeDao: RecipeDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(recipeDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}