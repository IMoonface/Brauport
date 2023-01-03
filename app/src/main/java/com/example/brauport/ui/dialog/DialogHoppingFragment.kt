package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.HoppingAdapter
import com.example.brauport.mapper.toStockItem
import com.example.brauport.model.recipe.Hopping
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType.HOP
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem
import com.example.brauport.ui.viewModel.StockViewModel
import com.example.brauport.ui.viewModel.StockViewModelFactory
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogHoppingBinding
import kotlinx.coroutines.launch

class DialogHoppingFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogHoppingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HoppingAdapter
    private lateinit var stockList: List<StockItem>

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogHoppingBinding.inflate(inflater, container, false)

        adapter = HoppingAdapter()
        binding.rHoppingRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                stockList = it.map { it.toStockItem() }.filter { it.itemType == HOP.ordinal }
                adapter.submitList(stockList)
            }
        }

        binding.rHoppingConfirmButton.setOnClickListener {
            val newTime = binding.rHoppingTimeInput.text.toString()

            if (newTime == "")
                Toast.makeText(context, R.string.enter_time, Toast.LENGTH_SHORT).show()
            else if (adapter.hopList.isEmpty())
                Toast.makeText(context, R.string.choose_hop, Toast.LENGTH_SHORT).show()
            else {
                recipeItem.hoppingList.add(Hopping(adapter.hopList, newTime))
                Toast.makeText(context, R.string.added_hopping, Toast.LENGTH_SHORT).show()
                adapter.hopList = mutableListOf()
            }
        }

        binding.rHoppingBackButton.setOnClickListener {
            dismiss()
        }

        binding.rHoppingDeleteButton.setOnClickListener {
            val index = recipeItem.hoppingList.count() - 1
            if (index != -1) {
                recipeItem.hoppingList.removeAt(index)
                Toast.makeText(context, R.string.deleted_hopping, Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(context, R.string.hopping_not_found, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}