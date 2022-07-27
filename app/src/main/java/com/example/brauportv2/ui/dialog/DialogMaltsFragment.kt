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
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.SNoAmount
import com.example.brauportv2.ui.`object`.RecipeDataSource.recipeItem
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
        _binding = FragmentDialogMaltsBinding.inflate(inflater, container, false)

        adapter = RecipeStockAdapter(this::onItemAdd, this::onItemDelete)
        binding.rMaltsRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.MALT.ordinal })
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
            Toast.makeText(context, "Malz schon vorhanden!", Toast.LENGTH_SHORT).show()
        else {
            item.stockAmount = amount
            recipeItem.maltList.add(item)
            Toast.makeText(context, "Malz hinzugefügt!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(item: SNoAmount) {
        val index = recipeItem.maltList.map { it.toSNoAmount() }.indexOf(item)
        if (index != -1) {
            recipeItem.maltList.removeAt(index)
            Toast.makeText(context, "Malz gelöscht!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Malz nicht vorhanden!", Toast.LENGTH_SHORT).show()
        }
    }
}