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
import com.example.brauportv2.databinding.FragmentDialogMaltsBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogMaltsFragment : DialogFragment() {

    private var _binding: FragmentDialogMaltsBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentDialogMaltsBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)
        binding.rMaltsRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> adapter.submitList(it.map { it.toStockItem() }
                .filter { it.itemType == StockItemType.MALT })
            }
        }

        binding.rMaltsAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(rStockItem: RStockItem) {
        if (recipeItem.maltList.contains(rStockItem))
            Toast.makeText(context, "Malz schon vorhanden", Toast.LENGTH_SHORT).show()
        else {
            recipeItem.maltList.add(rStockItem)
            Toast.makeText(context, "Malz hinzugefügt", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(rStockItem: RStockItem) {
        if (recipeItem.maltList.remove(rStockItem)) {
            Toast.makeText(context, "Malz gelöscht", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Malz nicht vorhanden", Toast.LENGTH_SHORT).show()
        }
    }
}