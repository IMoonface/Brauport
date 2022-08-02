package com.example.brauportv2

import android.app.Application
import com.example.brauportv2.data.brewHistory.BrewHistoryDatabase
import com.example.brauportv2.data.recipe.RecipeDatabase
import com.example.brauportv2.data.stock.StockDatabase

class BaseApplication : Application() {
    val stockDatabase: StockDatabase by lazy { StockDatabase.getDatabase(this) }
    val recipeDatabase: RecipeDatabase by lazy { RecipeDatabase.getDatabase(this) }
    val brewHistoryDatabase: BrewHistoryDatabase by lazy { BrewHistoryDatabase.getDatabase(this) }
}