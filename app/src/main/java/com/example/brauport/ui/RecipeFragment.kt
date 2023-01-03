package com.example.brauport.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.RecipeAdapter
import com.example.brauport.mapper.toBrewHistoryItem
import com.example.brauport.mapper.toRecipeItem
import com.example.brauport.model.brewHistory.BrewHistoryItem
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType
import com.example.brauport.ui.dialog.DialogDeleteFragment
import com.example.brauport.ui.dialog.DialogInstructionRecipeFragment
import com.example.brauport.ui.dialog.DialogRecipeInspectFragment
import com.example.brauport.ui.`object`.TextWatcherLogic.filterListForRecipe
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import com.example.brauportv2.databinding.FragmentRecipeBinding
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var startList: List<RecipeItem>
    private lateinit var adapter: RecipeAdapter

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            filterListForRecipe(binding.recipeTextInput.text.toString(), adapter, startList)
        }
    }

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
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        adapter = RecipeAdapter(this::onInspectClick, this::onItemClick, this::onDeleteClick)
        binding.recipeRecyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { recipeItemDataList ->
                startList = recipeItemDataList.map { it.toRecipeItem() }
                adapter.submitList(startList)
            }
        }

        binding.recipeAddButton.setOnClickListener {
            recipeItem = RecipeItem(
                rId = 0,
                recipeName = "",
                maltList = mutableListOf(),
                restList = mutableListOf(),
                hoppingList = mutableListOf(),
                yeast = StockItem(0, StockItemType.YEAST.ordinal, "", ""),
                mainBrew = MainBrew("", "")
            )

            findNavController().navigate(
                RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment()
            )
        }

        binding.recipeInfoButton.setOnClickListener {
            val dialog = DialogInstructionRecipeFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "recipeInfoDialog")
        }

        binding.recipeTextInput.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInspectClick(recipe: RecipeItem) {
        val dialog = DialogRecipeInspectFragment(recipe.toBrewHistoryItem(), false)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeInspectDialog")
    }

    private fun onItemClick(recipe: RecipeItem) {
        recipeItem = recipe

        findNavController().navigate(
            RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment(true)
        )
    }

    private fun onDeleteClick(item: RecipeItem) {
        val dialog = DialogDeleteFragment(item.toBrewHistoryItem(), this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: BrewHistoryItem) {
        viewModel.deleteRecipe(item.toRecipeItem())
    }

    companion object {
        lateinit var recipeItem : RecipeItem
    }
}