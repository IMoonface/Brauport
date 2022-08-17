package com.example.brauportv2.data.step

import androidx.room.*
import com.example.brauportv2.model.brew.StepListData
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Query("SELECT * from step_database ORDER BY sId ASC")
    fun getAllStepLists(): Flow<List<StepListData>>

    @Query("SELECT * from step_database WHERE recipeId = :id")
    fun getStepList(id: Int): Flow<StepListData>

    @Insert
    suspend fun insert(item: StepListData)

    @Update
    suspend fun update(item: StepListData)

    @Delete
    suspend fun delete(item: StepListData)
}