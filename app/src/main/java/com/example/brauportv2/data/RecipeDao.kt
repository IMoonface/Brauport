package com.example.brauportv2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.example.brauportv2.model.recipeModel.RecipeItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe_database ORDER BY recipeName ASC")
    fun getAllRecipeItems(): Flow<List<RecipeItemData>>

    @Query("SELECT * from recipe_database WHERE rId = :id")
    fun getRecipeItem(id: Long): Flow<RecipeItemData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeItem: RecipeItemData)

    @Update
    suspend fun update(recipeItem: RecipeItemData)

    @Delete
    suspend fun delete(recipeItem: RecipeItemData)
}