package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.RecipeStockAdapter
import com.example.brauportv2.databinding.FragmentDialogYeastBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.stock.SNoAmount
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.YEAST
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.startYeast
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogYeastFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogYeastBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeStockAdapter

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogYeastBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)

        binding.rYeastRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }.filter {
                    it.itemType == YEAST.ordinal
                })
            }
        }

        binding.rYeastBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(item: StockItem, amount: String) {
        if (recipeItem.yeast.stockName != "" && recipeItem.yeast.stockAmount != "")
            Toast.makeText(context, R.string.only_one_yeast_per_recipe, Toast.LENGTH_SHORT)
                .show()
        else {
            item.stockAmount = amount + "g"
            recipeItem.yeast = item
            Toast.makeText(context, R.string.added_yeast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: SNoAmount) {
        if (recipeItem.yeast == startYeast) {
            Toast.makeText(context, R.string.yeast_not_found, Toast.LENGTH_SHORT).show()
        } else {
            recipeItem.yeast = StockItem(1, YEAST.ordinal, "", "")
            Toast.makeText(context, R.string.deleted_yeast, Toast.LENGTH_SHORT).show()
        }
    }
}