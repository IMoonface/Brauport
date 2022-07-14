package com.example.brauportv2.ui

import android.os.Bundle
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
import com.example.brauportv2.model.recipeModel.Recipe
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recipeStartList: List<RecipeItem>
    private lateinit var adapter: RecipeAdapter

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        adapter = RecipeAdapter()
        binding.recipeRecyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { it ->
                recipeStartList = it.map { it.toRecipeItem() }
                adapter.submitList(recipeStartList)
            }
        }

        binding.recipeAddButton.setOnClickListener {
            onRecipeClick()
            viewModel.addRecipe(Recipe.recipeItem)
        }

        return binding.root
    }

    private fun onRecipeClick() {
        val action = RecipeFragmentDirections
            .actionRecipeFragmentToRecipeDetailsFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}