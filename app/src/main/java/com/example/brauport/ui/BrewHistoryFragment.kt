package com.example.brauport.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.BrewHistoryAdapter
import com.example.brauport.databinding.FragmentBrewHistoryBinding
import com.example.brauport.mapper.toRecipeItem
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.ui.dialog.DialogCookingFragment
import com.example.brauport.ui.dialog.DialogDeleteFragment
import com.example.brauport.ui.dialog.DialogRecipeInspectFragment
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewHistoryAdapter

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication).recipeDatabase.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        adapter = BrewHistoryAdapter(this::onInspectItem, this::onItemClick, this::onDeleteClick)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { recipeItemData ->
                adapter.submitList(recipeItemData.map { it.toRecipeItem() }.filter {
                    it.isBrewHistoryItem
                })
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInspectItem(item: RecipeItem) {
        val dialog = DialogRecipeInspectFragment(item)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "inspectDialog")
    }

    private fun onItemClick(item: RecipeItem) {
        val dialog = DialogCookingFragment(true, item, this::onDialogCookingDismiss)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onDeleteClick(item: RecipeItem) {
        val dialog = DialogDeleteFragment(item, true, this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: RecipeItem, isChecked: Boolean) {
        if (isChecked)
            viewModel.updateRecipe(
                id = item.id,
                name = item.name,
                maltList = item.maltList,
                restList = item.restList,
                hoppingList = item.hoppingList,
                yeast = item.yeast,
                mainBrew = item.mainBrew,
                dateOfCompletion = item.dateOfCompletion,
                endOfFermentation = item.endOfFermentation,
                cardColor = item.cardColor,
                isBrewHistoryItem = false,
                isRecipeItem = item.isRecipeItem
            )
        else
            viewModel.deleteRecipe(item)
    }

    private fun onDialogCookingDismiss() {}
}