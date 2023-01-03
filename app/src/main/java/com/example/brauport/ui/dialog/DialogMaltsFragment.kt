package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.RecipeStockAdapter
import com.example.brauport.mapper.toSNoAmount
import com.example.brauport.mapper.toStockItem
import com.example.brauport.model.stock.SNoAmount
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType.MALT
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem
import com.example.brauport.ui.viewModel.StockViewModel
import com.example.brauport.ui.viewModel.StockViewModelFactory
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogMaltsBinding
import kotlinx.coroutines.launch

class DialogMaltsFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogMaltsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeStockAdapter
    private lateinit var stockList: List<StockItem>

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogMaltsBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)
        binding.rMaltsRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                stockList = it.map { it.toStockItem() }.filter { it.itemType == MALT.ordinal }
                adapter.submitList(stockList)
            }
        }

        binding.rMaltsBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(item: StockItem, amount: String) {
        if (recipeItem.maltList.map { it.toSNoAmount() }.contains(item.toSNoAmount()))
            Toast.makeText(context, R.string.malt_exists, Toast.LENGTH_SHORT).show()
        else {
            item.stockAmount = amount
            recipeItem.maltList.add(item)
            Toast.makeText(context, R.string.added_malt, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: SNoAmount) {
        val index = recipeItem.maltList.map { it.toSNoAmount() }.indexOf(item)
        if (index != -1) {
            recipeItem.maltList.removeAt(index)
            Toast.makeText(context, R.string.deleted_malt, Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(context, R.string.malt_not_found, Toast.LENGTH_SHORT).show()
    }
}