package com.example.brauportv2.model.recipeModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe_database")
data class RecipeItemData(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "recipeName")
    val recipeName: String,
    @ColumnInfo(name = "maltList")
    val rMaltList: List<RStockItem>,
    @ColumnInfo(name = "rest")
    val rRest: Rest,
    @ColumnInfo(name = "hoppingList")
    val rHoppingList: List<Hopping>,
    @ColumnInfo(name = "yeast")
    val rYeast: RStockItem,
    @ColumnInfo(name = "brew")
    val rMainBrew: MainBrew
)
