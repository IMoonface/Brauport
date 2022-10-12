package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogStockBinding
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import java.util.*

class DialogStockFragment(
    private val stockId: Int,
    private val itemType: Int,
    private val update: Boolean
) : BaseDialogFragment() {

    private var _binding: FragmentDialogStockBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogStockBinding.inflate(inflater, container, false)

        binding.stockConfirmButton.setOnClickListener {
            val itemTitle = binding.stockItemName.text.toString()
            val itemAmount = binding.stockItemAmount.text.toString()

            if (itemTitle == "" || itemAmount == "") {
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            } else {
                if (update) {
                    viewModel.updateStock(
                        id = stockId,
                        itemType = itemType,
                        stockName = itemTitle,
                        stockAmount = itemAmount
                    )
                } else
                    viewModel.addStock(
                        StockItem(
                            id = UUID.randomUUID().hashCode(),
                            itemType = itemType,
                            stockName = itemTitle,
                            stockAmount = itemAmount
                        )
                    )
                dismiss()
            }
        }

        binding.stockAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}