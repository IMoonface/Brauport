package com.example.brauportv2.ui

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
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.RecipeAdapter
import com.example.brauportv2.databinding.FragmentRecipeBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.recipe.Rest
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType
import com.example.brauportv2.ui.dialog.DialogDeleteFragment
import com.example.brauportv2.ui.dialog.DialogInstructionRecipeFragment
import com.example.brauportv2.ui.dialog.DialogRecipeInspectFragment
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.update
import com.example.brauportv2.ui.objects.TextWatcherLogic.filterListForRecipe
import com.example.brauportv2.ui.viewModel.RecipeViewModel
import com.example.brauportv2.ui.viewModel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeStartList: List<RecipeItem>
    private lateinit var adapter: RecipeAdapter

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            filterListForRecipe(binding.recipeTextInput.text.toString(), adapter, recipeStartList)
        }
    }

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication)
                .recipeDatabase.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        adapter = RecipeAdapter(this::onInspectClick, this::onItemClick, this::onDeleteClick)
        binding.recipeRecyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { it ->
                recipeStartList = it.map { it.toRecipeItem() }
                adapter.submitList(recipeStartList)
            }
        }

        binding.recipeAddButton.setOnClickListener {
            update = false
            recipeItem = RecipeItem(
                1,
                "",
                emptyList<StockItem>().toMutableList(),
                emptyList<Rest>().toMutableList(),
                emptyList<Hopping>().toMutableList(),
                StockItem(1, StockItemType.YEAST.ordinal, "", ""),
                MainBrew("", "")
            )

            val action = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment()
            findNavController().navigate(action)
        }

        binding.recipeInfoButton.setOnClickListener {
            val dialog = DialogInstructionRecipeFragment()
            dialog.show(childFragmentManager, "recipeInfoDialog")
        }

        binding.recipeTextInput.addTextChangedListener(textWatcher)

        return binding.root
    }

    private fun onInspectClick(recipe: RecipeItem) {
        val dialog = DialogRecipeInspectFragment(recipe.toBrewHistoryItem(), false)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeInspectDialog")
    }

    private fun onItemClick(recipe: RecipeItem) {
        update = true
        recipeItem = recipe

        val action = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment()
        findNavController().navigate(action)
    }

    private fun onDeleteClick(item: RecipeItem) {
        val dialog = DialogDeleteFragment(item, this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: RecipeItem) {
        viewModel.deleteRecipe(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}