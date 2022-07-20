package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.RecipeHoppingAdapter
import com.example.brauportv2.databinding.FragmentDialogHopsBinding
import com.example.brauportv2.mapper.toRStockItem
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogHopsFragment : DialogFragment() {

    private var _binding: FragmentDialogHopsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RecipeHoppingAdapter

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogHopsBinding.inflate(inflater, container, false)

        adapter = RecipeHoppingAdapter(this::onItemAdd, this::onItemDelete)
        binding.rHoppingRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> adapter.submitList(it.map { it.toStockItem() }
                .filter { it.itemType == StockItemType.HOP })
            }
        }

        binding.rHoppingAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(rStockItem: RStockItem, time: String) {
        val onlyHops = recipeItem.hoppingList.map { it.toRStockItem() }
        if (onlyHops.contains(rStockItem)) {
            val index = onlyHops.indexOf(rStockItem)
            recipeItem.hoppingList[index].hoppingTime = time
            Toast.makeText(
                context,
                "Hopfengabe schon vorhanden, Zeit wurde aktualisiert",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            recipeItem.hoppingList.add(
                Hopping(
                    rStockItem.rStockName,
                    rStockItem.rItemType,
                    rStockItem.rStockAmount,
                    time
                )
            )
            Toast.makeText(context, "Hopfengabe hinzugefügt", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete(rStockItem: RStockItem) {
        val index = recipeItem.hoppingList.map { it.toRStockItem() }.indexOf(rStockItem)
        if (index != -1) {
            recipeItem.hoppingList.removeAt(index)
            Toast.makeText(context, "Hopfengabe gelöscht", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Hopfengabe nicht vorhanden", Toast.LENGTH_SHORT).show()
        }
    }
}