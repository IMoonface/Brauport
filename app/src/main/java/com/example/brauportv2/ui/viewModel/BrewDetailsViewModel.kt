package com.example.brauportv2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.brauportv2.data.step.StepDao
import com.example.brauportv2.mapper.toStepListData
import com.example.brauportv2.model.brew.StepItem
import com.example.brauportv2.model.brew.StepList
import com.example.brauportv2.model.brew.StepListData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class BrewDetailsViewModel(private val stepDao: StepDao) : ViewModel() {

    val allStepLists: Flow<List<StepListData>> = stepDao.getAllStepLists()

    fun addStepList(item: StepList) {
        viewModelScope.launch {
            stepDao.insert(item.toStepListData())
        }
    }

    fun updateStepList(
        sId: Int,
        rId: Int,
        steps: List<StepItem>
    ) {
        val item = StepList(
            sId = sId,
            rId = rId,
            steps = steps
        )
        viewModelScope.launch(Dispatchers.IO) {
            stepDao.update(item.toStepListData())
        }
    }

    fun deleteStepList(item: StepList) {
        viewModelScope.launch(Dispatchers.IO) {
            stepDao.delete(item.toStepListData())
        }
    }

    fun minutes(millis: Long): String {
        if (millis / 60000 < 1) return "00"
        if (millis / 60000 in 1..9) return "0" + (millis / 60000)
        return "" + (millis / 60000)
    }

    fun seconds(millis: Long): String {
        var millisSeconds: Long = millis
        while (millisSeconds >= 60000)
            millisSeconds -= 60000

        return when (millisSeconds / 1000) {
            in 0..0 -> "00"
            in 1..9 -> "0" + +(millisSeconds / 1000)
            else -> "" + millisSeconds / 1000
        }
    }
}

class BrewDetailsViewModelFactory(private val stepDao: StepDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrewDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BrewDetailsViewModel(stepDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}