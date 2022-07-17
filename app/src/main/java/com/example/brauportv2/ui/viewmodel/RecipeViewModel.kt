package com.example.brauportv2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.RecipeDao
import com.example.brauportv2.mapper.toRecipeItemData
import com.example.brauportv2.model.recipeModel.*
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
        rMaltList: MutableList<RStockItem>,
        rRest: Rest,
        rHoppingList: MutableList<RStockItem>,
        rTimeList: MutableList<String>,
        rYeast: RStockItem,
        rMainBrew: MainBrew
    ) {
        val recipeItem = RecipeItem(
            rId = rId,
            recipeName = recipeName,
            rMaltList = rMaltList,
            rRest = rRest,
            rHopsList = rHoppingList,
            rTimeList = rTimeList,
            rYeast = rYeast,
            rMainBrew = rMainBrew
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