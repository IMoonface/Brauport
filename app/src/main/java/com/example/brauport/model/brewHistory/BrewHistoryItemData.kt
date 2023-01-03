package com.example.brauport.model.brewHistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauport.model.recipe.Hopping
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.Rest
import com.example.brauport.model.stock.StockItem

@Entity(tableName = "brew_history_database")
data class BrewHistoryItemData(
    @PrimaryKey
    val bId: Int,
    @ColumnInfo(name = "brewHistoryItemName")
    val bName: String,
    @ColumnInfo(name = "maltList")
    val bMaltList: List<StockItem>,
    @ColumnInfo(name = "restList")
    val bRestList: List<Rest>,
    @ColumnInfo(name = "hoppingList")
    val bHoppingList: List<Hopping>,
    @ColumnInfo(name = "yeast")
    val bYeast: StockItem,
    @ColumnInfo(name = "mainBrew")
    val bMainBrew: MainBrew,
    @ColumnInfo(name = "dateOfCompletion")
    val bDateOfCompletion: String,
    @ColumnInfo(name = "endOfFermentation")
    val bEndOfFermentation: String,
    @ColumnInfo(name = "cardColor")
    var cardColor: Int
)
