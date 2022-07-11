package com.example.brauportv2.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.databinding.FragmentYeastStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class YeastStockFragment : Fragment() {

    private var _binding: FragmentYeastStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private lateinit var yeastStartList: List<StockItem>
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            val textInputText = binding.yeastTextInputEditText.text.toString()

            if (textInputText != "" && textInputText.endsWith("g")) {
                adapter.submitList(yeastStartList.filter {
                    it.stockAmount.removeSuffix("g").toInt() <=
                            textInputText.removeSuffix("g").toInt()
                })
            } else if (textInputText != "")
                adapter.submitList(yeastStartList.filter { it.stockName.contains(textInputText) })
            else
                adapter.submitList(yeastStartList)
        }
    }

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).database.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentYeastStockBinding
            .inflate(inflater, container, false)
        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)
        binding.yeastRecyclerView.adapter = adapter
        //binding.yeastRecyclerView.hasFixedSize()

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                yeastStartList = it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.YEAST }
                adapter.submitList(yeastStartList)
            }
        }

        binding.yeastTextInputEditText.text?.clear()

        binding.yeastAddButton.setOnClickListener {
            openAddDialog()
        }

        binding.yeastTextInputEditText.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAddDialog() {
        val dialog = DialogStockFragment(
            StockItem(hashCode(), StockItemType.YEAST, "test", "test"),
            StockItemType.YEAST,
            false
        )

        dialog.show(childFragmentManager, "stockDialog")
    }

    private fun openUpdateDialog(stockItem: StockItem) {
        val dialog = DialogStockFragment(stockItem, StockItemType.YEAST, true)

        dialog.show(childFragmentManager, "stockDialog")
    }

    private fun onItemClick(stockItem: StockItem) {
        openUpdateDialog(stockItem)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}