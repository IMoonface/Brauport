package com.example.brauport

import android.app.Application
import com.example.brauport.data.recipe.RecipeDatabase
import com.example.brauport.data.stock.StockDatabase

class BaseApplication : Application() {
    val stockDatabase: StockDatabase by lazy { StockDatabase.getDatabase(this) }
    val recipeDatabase: RecipeDatabase by lazy { RecipeDatabase.getDatabase(this) }
}