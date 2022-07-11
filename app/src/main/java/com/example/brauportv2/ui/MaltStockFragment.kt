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
import com.example.brauportv2.databinding.FragmentMaltStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class MaltStockFragment : Fragment() {

    private var _binding: FragmentMaltStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter
    private lateinit var maltStartList: List<StockItem>
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {

            val textInputText = binding.maltTextInputEditText.text.toString()

            if (textInputText != "" && textInputText.endsWith("g")) {
                adapter.submitList(maltStartList.filter {
                    it.stockAmount.removeSuffix("g").toInt()<=
                            textInputText.removeSuffix("g").toInt()
                })
            } else if (textInputText != "")
                adapter.submitList(maltStartList.filter { it.stockName.contains(textInputText) })
            else
                adapter.submitList(maltStartList)
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
        _binding = FragmentMaltStockBinding
            .inflate(inflater, container, false)

        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)

        binding.maltRecyclerView.adapter = adapter
        //binding.maltRecyclerView.hasFixedSize()

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                maltStartList = it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.MALT }
                adapter.submitList(maltStartList)
            }
        }

        binding.maltNextButton.setOnClickListener {

            val action = MaltStockFragmentDirections.actionMaltStockFragmentToHopStockFragment()
            findNavController().navigate(action)

            binding.maltTextInputEditText.text?.clear()
        }

        binding.maltAddButton.setOnClickListener {
            openAddDialog()
        }

        binding.maltTextInputEditText.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAddDialog() {
        val dialog = DialogStockFragment(
            StockItem(hashCode(), StockItemType.MALT, "test", "test"),
            StockItemType.MALT,
            false
        )
        dialog.show(childFragmentManager, "stockDialog")
    }

    private fun openUpdateDialog(stockItem: StockItem) {
        val dialog = DialogStockFragment(stockItem, StockItemType.MALT, true)
        dialog.show(childFragmentManager, "stockDialog")
    }

    private fun onItemClick(stockItem: StockItem) {
        openUpdateDialog(stockItem)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}