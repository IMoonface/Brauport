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
import com.example.brauportv2.adapter.RecipeStockAdapter
import com.example.brauportv2.databinding.FragmentDialogYeastsBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.recipe.SNoAmount
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.YEAST
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.startYeast
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogYeastFragment : DialogFragment() {

    private var _binding: FragmentDialogYeastsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeStockAdapter
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
        _binding = FragmentDialogYeastsBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)

        binding.rYeastsRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }.filter {
                    it.itemType == YEAST.ordinal
                })
            }
        }

        binding.rYeastsBackButton.setOnClickListener {
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
            Toast.makeText(context, "Nur eine Hefe pro Rezept möglich!", Toast.LENGTH_SHORT)
                .show()
        else {
            item.stockAmount = amount + "g"
            recipeItem.yeast = item
            Toast.makeText(context, "Hefe hinzugefügt!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: SNoAmount) {
        if (recipeItem.yeast == startYeast) {
            Toast.makeText(context, "Hefe nicht vorhanden!", Toast.LENGTH_SHORT).show()
        } else {
            recipeItem.yeast = StockItem(1, YEAST.ordinal, "", "")
            Toast.makeText(context, "Hefe gelöscht!", Toast.LENGTH_SHORT).show()
        }
    }
}