package com.example.brauportv2.model.brew

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "step_database",
    indices = [Index(value = ["recipeId"], unique = true)]
)
data class StepListData(
    @PrimaryKey
    val sId: Int,
    @ColumnInfo(name = "recipeId")
    val rId: Int,
    @ColumnInfo(name = "steps")
    val steps: List<StepItem>
)
