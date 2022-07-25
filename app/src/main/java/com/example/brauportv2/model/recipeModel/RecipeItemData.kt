package com.example.brauportv2.model.recipeModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.brauportv2.model.StockItem

@Entity(
    tableName = "recipe_database",
    indices = [Index(value = ["recipeName"], unique = true)]
)
data class RecipeItemData(
    @PrimaryKey
    val rId: Int,
    @ColumnInfo(name = "recipeName")
    val recipeName: String,
    @ColumnInfo(name = "maltList")
    val maltList: List<StockItem>,
    @ColumnInfo(name = "rest")
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
    val endOfFermentation: String
)
