package com.example.brauportv2

import android.app.Application
import com.example.brauportv2.data.RecipeDatabase
import com.example.brauportv2.data.StockDatabase

class BaseApplication : Application() {
    val stockDatabase: StockDatabase by lazy { StockDatabase.getDatabase(this) }
    val recipeDatabase: RecipeDatabase by lazy { RecipeDatabase.getDatabase(this) }
}