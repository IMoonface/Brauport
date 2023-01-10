package com.example.brauport.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauport.model.stock.StockItem

@Entity(tableName = "recipe_database")
data class RecipeItemData(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "recipeName")
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
    var cardColor: Int,
    @ColumnInfo(name = "isBrewHistoryItem")
    var isBrewHistoryItem: Boolean,
    @ColumnInfo(name = "isRecipeItem")
    var isRecipeItem: Boolean
)
