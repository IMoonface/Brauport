package com.example.brauportv2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.recipe.RecipeDao
import com.example.brauportv2.mapper.toRecipeItemData
import com.example.brauportv2.model.recipe.*
import com.example.brauportv2.model.stock.StockItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class RecipeViewModel(private val recipeDao: RecipeDao) : ViewModel() {
    val allRecipeItems: Flow<List<RecipeItemData>> = recipeDao.getAllRecipeItems()

    fun addRecipe(recipeItem: RecipeItem) {
        viewModelScope.launch {
            recipeDao.insert(recipeItem.toRecipeItemData())
        }
    }

    fun updateRecipe(
        rId: Int,
        recipeName: String,
        maltList: MutableList<StockItem>,
        restList: MutableList<Rest>,
        hoppingList: MutableList<Hopping>,
        yeast: StockItem,
        mainBrew: MainBrew
    ) {
        val recipeItem = RecipeItem(
            rId = rId,
            recipeName = recipeName,
            maltList = maltList,
            restList = restList,
            hoppingList = hoppingList,
            yeast = yeast,
            mainBrew = mainBrew
        )
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.update(recipeItem.toRecipeItemData())
        }
    }

    fun deleteRecipe(recipeItem: RecipeItem) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeDao.delete(recipeItem.toRecipeItemData())
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