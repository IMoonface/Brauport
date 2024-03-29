package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauport.BaseApplication
import com.example.brauport.R
import com.example.brauport.adapter.RecipeStockAdapter
import com.example.brauport.databinding.FragmentDialogYeastBinding
import com.example.brauport.mapper.toStockItem
import com.example.brauport.model.stock.SNoAmount
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType.YEAST
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem
import com.example.brauport.ui.viewModel.StockViewModel
import com.example.brauport.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogYeastFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogYeastBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeStockAdapter

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogYeastBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }.filter {
                    it.itemType == YEAST.ordinal
                })
            }
        }

        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(item: StockItem, amount: String) {
        if (recipeItem.yeast.name != "" && recipeItem.yeast.amount != "")
            Toast.makeText(context, R.string.only_one_yeast_per_recipe, Toast.LENGTH_SHORT)
                .show()
        else {
            item.amount = amount
            recipeItem.yeast = item
            Toast.makeText(context, R.string.added_yeast, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: SNoAmount) {
        val startYeast = StockItem(
            id = 0, itemType = YEAST.ordinal, name = "", amount = ""
        )

        if (recipeItem.yeast == startYeast)
            Toast.makeText(context, R.string.yeast_not_found, Toast.LENGTH_SHORT).show()
        else {
            recipeItem.yeast = startYeast
            Toast.makeText(context, R.string.deleted_yeast, Toast.LENGTH_SHORT).show()
        }
    }
}