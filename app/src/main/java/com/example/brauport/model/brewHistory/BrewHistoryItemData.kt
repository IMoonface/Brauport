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
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "maltList")
    val maltList: List<StockItem>,
    @ColumnInfo(name = "restList")
    val restList: List<Rest>,
    @ColumnInfo(name = "hoppingList")
    val hoppingList: List<Hopping>,
    @ColumnInfo(name = "yeast")
    val yeast: StockItem,
    @ColumnInfo(name = "mainBrew")
    val mainBrew: MainBrew,
    @ColumnInfo(name = "dateOfCompletion")
    val dateOfCompletion: String,
    @ColumnInfo(name = "endOfFermentation")
    val endOfFermentation: String,
    @ColumnInfo(name = "cardColor")
    val cardColor: Int
)
