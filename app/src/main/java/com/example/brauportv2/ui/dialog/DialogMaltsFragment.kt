package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.RecipeStockAdapter
import com.example.brauportv2.databinding.FragmentDialogMaltsBinding
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.recipe.SNoAmount
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.MALT
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogMaltsFragment : DialogFragment() {

    private var _binding: FragmentDialogMaltsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeStockAdapter
    private lateinit var stockList: List<StockItem>
    private var newMaltList = mutableListOf<StockItem>()

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

        binding.rMaltsRefreshButton.setOnClickListener {
            recipeItem.maltList.forEach { stockItem ->
                if (stockList.map { it.toSNoAmount() }.contains(stockItem.toSNoAmount()))
                    newMaltList.add(stockItem)
            }
            recipeItem.maltList = newMaltList
            Toast.makeText(context, R.string.refresh_list, Toast.LENGTH_SHORT).show()
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
            item.stockAmount = amount + "g"
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