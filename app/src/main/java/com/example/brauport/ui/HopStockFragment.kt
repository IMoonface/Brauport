package com.example.brauport.ui

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
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.StockAdapter
import com.example.brauport.databinding.FragmentHopStockBinding
import com.example.brauport.mapper.toStockItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType.HOP
import com.example.brauport.ui.`object`.TextWatcherLogic.filterListForStock
import com.example.brauport.ui.dialog.DialogInstructionStockFragment
import com.example.brauport.ui.dialog.DialogStockFragment
import com.example.brauport.ui.viewModel.StockViewModel
import com.example.brauport.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class HopStockFragment : Fragment() {

    private var _binding: FragmentHopStockBinding? = null
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHopStockBinding.inflate(inflater, container, false)

        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)
        binding.recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { stockItemDataList ->
                startList = stockItemDataList.map { it.toStockItem() }.filter {
                    it.itemType == HOP.ordinal
                }
                adapter.submitList(startList)
            }
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(
                HopStockFragmentDirections.actionHopsStockFragmentToYeastStockFragment()
            )

            binding.textInput.text?.clear()
        }

        binding.beforeButton.setOnClickListener {
            findNavController().navigate(
                HopStockFragmentDirections.actionHopsStockFragmentToMaltStockFragment()
            )

            binding.textInput.text?.clear()
        }

        binding.addButton.setOnClickListener {
            val item = StockItem(
                id = UUID.randomUUID().hashCode(),
                itemType = HOP.ordinal,
                name = "",
                amount = ""
            )

            val dialog = DialogStockFragment(
                item, this::onAddClick, this::onItemUpdate, false
            )

            dialog.isCancelable = false
            dialog.show(childFragmentManager, "hopAddDialog")
        }

        binding.textInput.addTextChangedListener(textWatcher)

        binding.infoButton.setOnClickListener {
            val dialog = DialogInstructionStockFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "hopInfoDialog")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClick(item: StockItem) {
        val dialog = DialogStockFragment(item, this::onAddClick, this::onItemUpdate, true)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "hopUpdateDialog")
    }

    private fun onAddClick(item: StockItem) {
        viewModel.addStock(item)
    }

    private fun onItemUpdate(item: StockItem) {
        viewModel.updateStock(
            id = item.id,
            itemType = item.itemType,
            name = item.name,
            amount = item.amount
        )

        findNavController().navigate(HopStockFragmentDirections.actionHopStockFragmentSelf())
    }

    private fun onDeleteClick(item: StockItem) {
        viewModel.deleteStock(item)
    }
}