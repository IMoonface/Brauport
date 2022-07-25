package com.example.brauportv2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.adapter.BrewHistoryAdapter
import com.example.brauportv2.databinding.FragmentBrewHistoryBinding
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : BrewHistoryAdapter
    private lateinit var brewHistoryList : List<RecipeItem>
    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        adapter = BrewHistoryAdapter()
        binding.brewHistoryRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { it -> brewHistoryList = it.map { it.toRecipeItem() }
                .filter { it.dateOfCompletion != "" && it.endOfFermentation != "" }
                adapter.submitList(brewHistoryList)
            }
        }

        return binding.root
    }
}