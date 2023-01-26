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
import com.example.brauport.databinding.FragmentRecipeBinding
import com.example.brauport.mapper.toBrewHistoryItem
import com.example.brauport.mapper.toRecipeItem
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType
import com.example.brauport.ui.`object`.TextWatcherLogic.filterListForRecipe
import com.example.brauport.ui.dialog.DialogDeleteFragment
import com.example.brauport.ui.dialog.DialogInstructionRecipeFragment
import com.example.brauport.ui.dialog.DialogRecipeInspectFragment
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeList: List<RecipeItem>
    private lateinit var adapter: RecipeAdapter

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            filterListForRecipe(binding.textInput.text.toString(), adapter, recipeList)
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

        binding.recyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { recipeItemDataList ->
                recipeList = recipeItemDataList.map { it.toRecipeItem() }
                adapter.submitList(recipeList)
            }
        }

        binding.addButton.setOnClickListener {
            recipeItem = RecipeItem(
                id = 0,
                name = "",
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

        binding.infoButton.setOnClickListener {
            val dialog = DialogInstructionRecipeFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "recipeInfoDialog")
        }

        binding.textInput.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInspectClick(item: RecipeItem) {
        val dialog = DialogRecipeInspectFragment(item.toBrewHistoryItem(), false)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeInspectDialog")
    }

    private fun onItemClick(item: RecipeItem) {
        recipeItem = item

        findNavController().navigate(
            RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment(true)
        )
    }

    private fun onDeleteClick(item: RecipeItem) {
        val dialog = DialogDeleteFragment(item, this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: RecipeItem) {
        viewModel.deleteRecipe(item)
    }

    companion object {
        lateinit var recipeItem: RecipeItem
    }
}