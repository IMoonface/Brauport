package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.RecipeHoppingAdapter
import com.example.brauportv2.databinding.FragmentDialogHopsBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.Recipe
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogHopsFragment : DialogFragment() {

    private var _binding: FragmentDialogHopsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeHoppingAdapter

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogHopsBinding.inflate(inflater, container, false)

        adapter = RecipeHoppingAdapter(this::onItemAdd)
        binding.rHoppingRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.HOP })
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(stockItem: StockItem, time: String) {
        Recipe.rHoppingList.add(Hopping(stockItem.stockName, stockItem.stockAmount, time))
    }
}