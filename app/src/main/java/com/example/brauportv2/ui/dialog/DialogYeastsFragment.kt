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
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.RSNoAmount
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.startYeast
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogYeastsFragment : DialogFragment() {

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
    ): View? {
        _binding = FragmentDialogYeastsBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)
        binding.rYeastsRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.YEAST })
            }
        }

        binding.rYeastsAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(item: RStockItem, amount: String) {
        if (recipeItem.yeast.rStockName != "" && recipeItem.yeast.rStockAmount != "")
            Toast.makeText(context, "Nur eine Hefe pro Rezept möglich!", Toast.LENGTH_SHORT).show()
        else {
            recipeItem.yeast = RStockItem(item.rStockName, item.rItemType, amount)
            Toast.makeText(context, "Hefe hinzugefügt!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: RSNoAmount) {
        if (recipeItem.yeast == startYeast) {
            Toast.makeText(context, "Hefe nicht vorhanden!", Toast.LENGTH_SHORT).show()
        } else {
            recipeItem.yeast = RStockItem("", StockItemType.YEAST.ordinal, "")
            Toast.makeText(context, "Hefe gelöscht!", Toast.LENGTH_SHORT).show()
        }
    }
}