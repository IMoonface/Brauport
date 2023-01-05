package com.example.brauport.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.brauport.BaseApplication
import com.example.brauport.databinding.FragmentHomeBinding
import com.example.brauport.mapper.toRecipeItem
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { recipeItemDataList ->
                spinnerItemList = recipeItemDataList.map { it.toRecipeItem() }
            }
        }

        binding.recipesButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToRecipeFragment()
            )
        }

        binding.stockButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToStockFragment()
            )
        }

        binding.brewButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToBrewFragment()
            )
        }

        binding.brewHistoryButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToBrewHistoryFragment()
            )
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var spinnerItemList: List<RecipeItem> = emptyList()
    }
}