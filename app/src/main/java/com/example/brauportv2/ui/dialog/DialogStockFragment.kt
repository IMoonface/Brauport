package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentDialogStockBinding
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import java.util.*

class DialogStockFragment(
    private val stockItemId: Int,
    private val itemType: StockItemType,
    private val update: Boolean
) : DialogFragment() {

    private var _binding: FragmentDialogStockBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogStockBinding.inflate(inflater, container, false)

        binding.stockAddButton.setOnClickListener {
            val itemTitle = binding.stockItemName.text.toString()
            val itemAmount = binding.stockItemAmount.text.toString() + "g"

            if (itemTitle == "" || itemAmount == "") {
                Toast.makeText(context, "Bitte Alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            } else {
                if (update)
                    viewModel.updateStock(stockItemId, itemType, itemTitle, itemAmount)
                else
                    viewModel.addStock(
                        StockItem(UUID.randomUUID().hashCode(), itemType, itemTitle, itemAmount)
                    )
                dismiss()
            }
        }

        binding.abortDialog.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}