package com.example.brauport.data.recipe

import androidx.room.*
import com.example.brauport.model.recipe.RecipeItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe_database ORDER BY name ASC")
    fun getAllRecipeItems(): Flow<List<RecipeItemData>>

    @Insert
    suspend fun insert(item: RecipeItemData)

    @Update
    suspend fun update(item: RecipeItemData)

    @Delete
    suspend fun delete(item: RecipeItemData)
}