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
import com.example.brauportv2.databinding.FragmentHopStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.dialog.DialogInstructionStockFragment
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class HopStockFragment : Fragment() {

    private var _binding: FragmentHopStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private lateinit var hopStartList: List<StockItem>
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            val textInputText = binding.hopTextInput.text.toString()

            if (textInputText != "" && textInputText.endsWith("g")) {
                adapter.submitList(hopStartList.filter {
                    it.stockAmount.removeSuffix("g").toInt() <=
                            textInputText.removeSuffix("g").toInt()
                })
            } else if (textInputText != "")
                adapter.submitList(hopStartList.filter { it.stockName.contains(textInputText) })
            else
                adapter.submitList(hopStartList)
        }
    }

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHopStockBinding
            .inflate(inflater, container, false)
        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)
        binding.hopRecyclerView.adapter = adapter
        //binding.hopRecyclerView.hasFixedSize()

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> hopStartList = it.map { it.toStockItem() }
                .filter { it.itemType == StockItemType.HOP }
                adapter.submitList(hopStartList)
            }
        }

        binding.hopNextButton.setOnClickListener {
            val action = HopStockFragmentDirections
                .actionHopStockFragmentToYeastStockFragment()
            findNavController().navigate(action)

            binding.hopTextInput.text?.clear()
        }

        binding.hopBeforeButton.setOnClickListener {
            val action = HopStockFragmentDirections
                .actionHopStockFragmentToMaltStockFragment()
            findNavController().navigate(action)

            binding.hopTextInput.text?.clear()
        }

        binding.hopAddButton.setOnClickListener {
            openAddDialog()
        }

        binding.hopTextInput.addTextChangedListener(textWatcher)

        binding.hopInfoButton.setOnClickListener {
            val dialog = DialogInstructionStockFragment()
            dialog.show(childFragmentManager, "hopInfoDialog")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAddDialog() {
        val dialog = DialogStockFragment(hashCode(), StockItemType.HOP, false)
        dialog.show(childFragmentManager, "hopAddDialog")
    }

    private fun openUpdateDialog(stockItem: StockItem) {
        val dialog = DialogStockFragment(stockItem.id, StockItemType.HOP, true)
        dialog.show(childFragmentManager, "hopUpdateDialog")
    }

    private fun onItemClick(stockItem: StockItem) {
        openUpdateDialog(stockItem)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}