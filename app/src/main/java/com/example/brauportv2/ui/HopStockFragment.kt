package com.example.brauportv2.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.R
import com.example.brauportv2.adapter.StockAdapter
import com.example.brauportv2.databinding.FragmentHopStockBinding
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType

class HopStockFragment : Fragment() {

    private var _binding: FragmentHopStockBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StockAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHopStockBinding
            .inflate(inflater, container, false)
        adapter = StockAdapter(this::onItemClick)
        binding.hopRecyclerView.adapter = adapter
        binding.hopRecyclerView.hasFixedSize()
        binding.hopNextButton.setOnClickListener {
            val action = HopStockFragmentDirections
                .actionHopStockFragmentToYeastStockFragment()
            findNavController().navigate(action)
        }
        binding.hopAddButton.setOnClickListener {
            openDialog(false, 0)
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

    private fun openDialog(update: Boolean, position: Int) {
        val viewDialog = View.inflate(context, R.layout.dialog_stock, null)

        val dialog = createDialog(context, viewDialog)

        viewDialog.findViewById<Button>(R.id.stock_add_button).setOnClickListener {
            val itemTitle = viewDialog.findViewById<EditText>(R.id.stock_item_name).text.toString()
            val itemAmount= viewDialog.findViewById<EditText>(R.id.stock_item_amount).text.toString()

            if (itemTitle == "" || itemAmount == "") {
                Toast.makeText(context, "Bitte Felder ausfüllen", Toast.LENGTH_SHORT).show()
            } else {
                val updatedList = adapter.currentList.toMutableList()
                if (update) {
                    updatedList.removeAt(position)
                }
                updatedList.add(StockItem(hashCode(), StockItemType.HOP, itemTitle, itemAmount))
                adapter.submitList(updatedList)
                dialog.dismiss()
            }
        }

        viewDialog.findViewById<Button>(R.id.abort_dialog).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun onItemClick(position: Int) {
        openDialog(true, position)
    }
}