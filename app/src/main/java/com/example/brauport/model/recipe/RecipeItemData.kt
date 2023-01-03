package com.example.brauport.model.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.brauport.model.stock.StockItem

@Entity(tableName = "recipe_database")
data class RecipeItemData(
    @PrimaryKey
    val rId: Int,
    @ColumnInfo(name = "recipeName")
    val recipeName: String,
    @ColumnInfo(name = "maltList")
    val maltList: List<StockItem>,
    @ColumnInfo(name = "restList")
    val restList: List<Rest>,
    @ColumnInfo(name = "hoppingList")
    val hoppingList: List<Hopping>,
    @ColumnInfo(name = "yeast")
    val yeast: StockItem,
    @ColumnInfo(name = "mainBrew")
    val mainBrew: MainBrew
)
