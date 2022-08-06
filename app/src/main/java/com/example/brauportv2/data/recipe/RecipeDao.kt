package com.example.brauportv2.data.recipe

import androidx.room.*
import com.example.brauportv2.model.recipe.RecipeItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * from recipe_database ORDER BY recipeName ASC")
    fun getAllRecipeItems(): Flow<List<RecipeItemData>>

    @Query("SELECT * from recipe_database WHERE rId = :id")
    fun getRecipeItem(id: Int): Flow<RecipeItemData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipeItem: RecipeItemData)

    @Update
    suspend fun update(recipeItem: RecipeItemData)

    @Delete
    suspend fun delete(recipeItem: RecipeItemData)
}