package com.example.brauportv2.model.brewHistory

import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.Rest
import com.example.brauportv2.model.stock.StockItem

data class BrewHistoryItem(
    var bId: Int,
    var bName: String,
    var bMaltList: List<StockItem>,
    var bRestList: List<Rest>,
    var bHoppingList: List<Hopping>,
    var bYeast: StockItem,
    var bMainBrew: MainBrew,
    var bDateOfCompletion: String,
    var bEndOfFermentation: String,
    var cardColor: Int
)
