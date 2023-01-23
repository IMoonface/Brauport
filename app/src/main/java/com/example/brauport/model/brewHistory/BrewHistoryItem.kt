package com.example.brauport.model.brewHistory

import com.example.brauport.model.recipe.Hopping
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.Rest
import com.example.brauport.model.stock.StockItem

data class BrewHistoryItem(
    var id: Int,
    val name: String,
    val maltList: MutableList<StockItem>,
    val restList: MutableList<Rest>,
    val hoppingList: MutableList<Hopping>,
    val yeast: StockItem,
    val mainBrew: MainBrew,
    var dateOfCompletion: String,
    var endOfFermentation: String,
    var cardColor: Int
)
