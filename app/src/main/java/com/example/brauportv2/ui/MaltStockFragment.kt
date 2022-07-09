package com.example.brauportv2.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.databinding.FragmentMaltStockBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class MaltStockFragment : Fragment() {

    private var _binding: FragmentMaltStockBinding? = null
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
        _binding = FragmentMaltStockBinding
            .inflate(inflater, container, false)
        adapter = StockAdapter(this::onItemClick, this::onDeleteClick)
        binding.maltRecyclerView.adapter = adapter
        binding.maltRecyclerView.hasFixedSize()

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == StockItemType.MALT })
            }
        }

        binding.maltNextButton.setOnClickListener {
            val action = MaltStockFragmentDirections
                .actionMaltStockFragmentToHopStockFragment()
            findNavController().navigate(action)
        }

        binding.maltAddButton.setOnClickListener {
            openDialog(false)
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

    //Ersetzbar durch Dialogfragment und für die Attribute dasselbe machen wie im StockAdapter
    private fun openDialog(editModus: Boolean) {
        val viewDialog = View.inflate(context, R.layout.dialog_stock, null)

        val dialog = createDialog(context, viewDialog)

        viewDialog.findViewById<Button>(R.id.stock_add_button).setOnClickListener {
            val itemTitle = viewDialog.findViewById<EditText>(R.id.stock_item_name).text.toString()
            val itemAmount = viewDialog.findViewById<EditText>(R.id.stock_item_amount).text.toString()

            if (itemTitle == "" || itemAmount == "")
                Toast.makeText(context, "Bitte Felder ausfüllen", Toast.LENGTH_SHORT).show()
            else {
                val newItem = StockItem(hashCode(), StockItemType.MALT, itemTitle, itemAmount)
                Log.i("test", newItem.toString())
                if (editModus)
                    viewModel.updateStock(hashCode(), StockItemType.MALT, itemTitle, itemAmount)
                else
                    viewModel.addStock(newItem)
                dialog.dismiss()
            }
        }

        viewDialog.findViewById<Button>(R.id.abort_dialog).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun onItemClick() {
        openDialog(true)
    }

    private fun onDeleteClick(stockItem: StockItem) {
        viewModel.deleteStock(stockItem)
    }
}