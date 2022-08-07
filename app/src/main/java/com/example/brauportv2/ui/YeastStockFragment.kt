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
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.databinding.FragmentYeastStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType.YEAST
import com.example.brauportv2.ui.dialog.DialogInstructionStockFragment
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.objects.TextWatcherLogic.filterListForStock
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class YeastStockFragment : Fragment() {

    private var _binding: FragmentYeastStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private lateinit var startList: List<StockItem>
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            filterListForStock(binding.yeastTextInput.text.toString(), adapter, startList)
        }
    }

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYeastStockBinding.inflate(inflater, container, false)

        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)

        binding.yeastRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                startList = it.map { it.toStockItem() }
                    .filter { it.itemType == YEAST.ordinal }
                adapter.submitList(startList)
            }
        }

        binding.yeastNextButton.setOnClickListener {
            val action = YeastStockFragmentDirections.actionYeastStockFragmentToMaltStockFragment()
            findNavController().navigate(action)

            binding.yeastTextInput.text?.clear()
        }

        binding.yeastBeforeButton.setOnClickListener {
            val action = YeastStockFragmentDirections.actionYeastStockFragmentToHopStockFragment()
            findNavController().navigate(action)

            binding.yeastTextInput.text?.clear()
        }

        binding.yeastAddButton.setOnClickListener {
            openAddDialog()
        }

        binding.yeastTextInput.addTextChangedListener(textWatcher)

        binding.yeastInfoButton.setOnClickListener {
            val dialog = DialogInstructionStockFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "yeastInfoDialog")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAddDialog() {
        val dialog = DialogStockFragment(hashCode(), YEAST.ordinal, false)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "stockAddDialog")
    }

    private fun openUpdateDialog(stockItem: StockItem) {
        val dialog = DialogStockFragment(stockItem.id, YEAST.ordinal, true)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "stockUpdateDialog")
    }

    private fun onItemClick(stockItem: StockItem) {
        openUpdateDialog(stockItem)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}