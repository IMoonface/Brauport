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
import com.example.brauportv2.adapter.HoppingAdapter
import com.example.brauportv2.databinding.FragmentDialogHoppingBinding
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.recipe.Hopping
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.HOP
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogHoppingFragment : DialogFragment() {

    private var _binding: FragmentDialogHoppingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HoppingAdapter
    private lateinit var stockList: List<StockItem>
    private var newHopsList = mutableListOf<StockItem>()

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
            onItemAdd()
        }

        binding.rHoppingBackButton.setOnClickListener {
            dismiss()
        }

        binding.rHoppingDeleteButton.setOnClickListener {
            onItemDelete()
        }

        binding.rHoppingRefreshButton.setOnClickListener {
            recipeItem.hoppingList.forEach { hopping ->
                hopping.hopsList.forEach { hop ->
                    if (stockList.map { it.toSNoAmount() }.contains(hop.toSNoAmount()))
                        newHopsList.add(hop)
                }
                hopping.hopsList = newHopsList
            }
            Toast.makeText(context, R.string.refresh_list, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd() {
        val newTime = binding.rHoppingTimeInput.text.toString()

        if (newTime == "")
            Toast.makeText(context, R.string.enter_time, Toast.LENGTH_SHORT).show()
        else {
            recipeItem.hoppingList.add(Hopping(adapter.hopsList, newTime))
            Toast.makeText(context, R.string.added_hopping, Toast.LENGTH_SHORT).show()
            adapter.hopsList = emptyList<StockItem>().toMutableList()
        }
    }

    private fun onItemDelete() {
        val index = recipeItem.hoppingList.count() - 1
        if (index != -1) {
            recipeItem.hoppingList.removeAt(index)
            Toast.makeText(context, R.string.deleted_hopping, Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(context, R.string.hopping_not_found, Toast.LENGTH_SHORT).show()
    }
}