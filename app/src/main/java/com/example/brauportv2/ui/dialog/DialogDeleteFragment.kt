package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentDialogDeleteBinding
import com.example.brauportv2.mapper.toStepList
import com.example.brauportv2.model.brew.StepList
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.stepList
import com.example.brauportv2.ui.viewModel.BrewDetailsViewModel
import com.example.brauportv2.ui.viewModel.BrewDetailsViewModelFactory
import kotlinx.coroutines.launch

class DialogDeleteFragment(
    private val item: RecipeItem,
    private val onDeleteConfirm: (RecipeItem) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogDeleteBinding? = null
    private val binding get() = _binding!!
    private var startList: List<StepList> = emptyList()

    private val viewModel: BrewDetailsViewModel by activityViewModels {
        BrewDetailsViewModelFactory(
            (activity?.application as BaseApplication).stepDatabase.stepDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.allStepLists.collect { list ->
                startList = list.map { it.toStepList() }.filter { it.rId == item.rId }
            }
        }

        binding.deleteYesButton.setOnClickListener {
            if (startList.isNotEmpty()) {
                viewModel.deleteStepList(startList[0])
                stepList = emptyList()
            }
            onDeleteConfirm(item)
            dismiss()
        }

        binding.deleteNoButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}