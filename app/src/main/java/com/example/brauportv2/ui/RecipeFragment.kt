package com.example.brauportv2.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.RecipeAdapter
import com.example.brauportv2.databinding.FragmentRecipeBinding
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.*
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeStartList: List<RecipeItem>
    private lateinit var adapter: RecipeAdapter
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            val textInputText = binding.recipeTextInput.text.toString()

            if (textInputText != "")
                adapter.submitList(recipeStartList.filter { it.recipeName.contains(textInputText) })
            else
                adapter.submitList(recipeStartList)
        }
    }

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        adapter = RecipeAdapter(this::onItemClick, this::onDeleteClick)
        binding.recipeRecyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { it -> recipeStartList = it.map { it.toRecipeItem() }
                adapter.submitList(recipeStartList)
            }
        }

        binding.recipeAddButton.setOnClickListener {
            Recipe.recipeItem = Recipe.recipeStartConfig
            onNewRecipeClick()
        }

        binding.recipeInfoButton.setOnClickListener {

        }

        binding.recipeTextInput.addTextChangedListener(textWatcher)

        return binding.root
    }

    private fun onNewRecipeClick() {
        val action = RecipeFragmentDirections
            .actionRecipeFragmentToRecipeDetailsFragment()
        findNavController().navigate(action)
    }

    private fun onItemClick(recipe: RecipeItem) {
        Recipe.recipeItem = recipe

        val action = RecipeFragmentDirections
            .actionRecipeFragmentToRecipeDetailsFragment()
        findNavController().navigate(action)
    }

    private fun onDeleteClick(recipe: RecipeItem) {
        viewModel.deleteRecipe(recipe)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}