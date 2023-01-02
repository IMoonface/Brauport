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
import com.example.brauportv2.ui.`object`.TextWatcherLogic.filterListForStock
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class YeastStockFragment : Fragment() {

    private var _binding: FragmentYeastStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private lateinit var startList: List<StockItem>

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            filterListForStock(binding.textInput.text.toString(), adapter, startList)
        }
    }

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYeastStockBinding.inflate(inflater, container, false)

        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { stockItemDataList ->
                startList = stockItemDataList.map { it.toStockItem() }.filter {
                    it.itemType == YEAST.ordinal
                }
                adapter.submitList(startList)
            }
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(
                YeastStockFragmentDirections.actionYeastStockFragmentToMaltStockFragment()
            )

            binding.textInput.text?.clear()
        }

        binding.beforeButton.setOnClickListener {
            findNavController().navigate(
                YeastStockFragmentDirections.actionYeastStockFragmentToHopsStockFragment()
            )

            binding.textInput.text?.clear()
        }

        binding.addButton.setOnClickListener {
            val item = StockItem(
                id = UUID.randomUUID().hashCode(),
                itemType = YEAST.ordinal,
                stockName = "",
                stockAmount = ""
            )

            val dialog = DialogStockFragment(
                item, this::onItemAdd, this::onItemUpdate, false
            )

            dialog.isCancelable = false
            dialog.show(childFragmentManager, "yeastAddDialog")
        }

        binding.textInput.addTextChangedListener(textWatcher)

        binding.infoButton.setOnClickListener {
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

    private fun onItemClick(item: StockItem) {
        val dialog = DialogStockFragment(item, this::onItemAdd, this::onItemUpdate, true)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "yeastUpdateDialog")
    }

    private fun onItemAdd(item: StockItem) {
        viewModel.addStock(item)
    }

    private fun onItemUpdate(item: StockItem) {
        viewModel.updateStock(
            id = item.id,
            itemType = item.itemType,
            stockName = item.stockName,
            stockAmount = item.stockAmount
        )

        findNavController().navigate(YeastStockFragmentDirections.actionYeastStockFragmentSelf())
    }

    private fun onDeleteClick(item: StockItem) {
        viewModel.deleteStock(item)
    }
}