package com.example.brauportv2.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.databinding.FragmentYeastStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class YeastStockFragment : Fragment() {

    private var _binding: FragmentYeastStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter

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
        binding.yeastRecyclerView.hasFixedSize()

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.YEAST })
            }
        }

        binding.yeastAddButton.setOnClickListener {
            openAddDialog()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDialog(context: Context?, viewDialog: View?): Dialog {
        val builder = AlertDialog.Builder(context)
        builder.setView(viewDialog)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }

    private fun openAddDialog() {
        val viewDialog = View.inflate(context, R.layout.dialog_stock, null)

        val dialog = createDialog(context, viewDialog)

        viewDialog.findViewById<Button>(R.id.stock_add_button).setOnClickListener {
            val itemTitle = viewDialog.findViewById<EditText>(R.id.stock_item_name).text.toString()
            val itemAmount= viewDialog.findViewById<EditText>(R.id.stock_item_amount).text.toString()

            if (itemTitle == "" || itemAmount == "")
                Toast.makeText(context, "Bitte Felder ausfüllen", Toast.LENGTH_SHORT).show()
            else {
                val newItem = StockItem(hashCode(), StockItemType.YEAST, itemTitle, itemAmount)
                viewModel.addStock(newItem)
                dialog.dismiss()
            }
        }

        viewDialog.findViewById<Button>(R.id.abort_dialog).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun openUpdateDialog(stockItem: StockItem) {
        val viewDialog = View.inflate(context, R.layout.dialog_stock, null)

        val dialog = createDialog(context, viewDialog)

        viewDialog.findViewById<Button>(R.id.stock_add_button).setOnClickListener {
            val itemTitle = viewDialog.findViewById<EditText>(R.id.stock_item_name).text.toString()
            val itemAmount= viewDialog.findViewById<EditText>(R.id.stock_item_amount).text.toString()

            if (itemTitle == "" || itemAmount == "")
                Toast.makeText(context, "Bitte Felder ausfüllen", Toast.LENGTH_SHORT).show()
            else {
                viewModel.updateStock(stockItem.id, stockItem.itemType, itemTitle, itemAmount)
                dialog.dismiss()
            }
        }

        viewDialog.findViewById<Button>(R.id.abort_dialog).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun onItemClick(stockItem: StockItem) {
        openUpdateDialog(stockItem)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}