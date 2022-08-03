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
import com.example.brauportv2.adapter.HoppingAdapter
import com.example.brauportv2.databinding.FragmentDialogHoppingBinding
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.stock.StockItemType.HOP
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class DialogHoppingFragment : DialogFragment() {

    private var _binding: FragmentDialogHoppingBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: HoppingAdapter

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
    ): View {
        _binding = FragmentDialogHoppingBinding.inflate(inflater, container, false)

        adapter = HoppingAdapter()
        binding.rHoppingRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it ->
                adapter.submitList(it.map { it.toStockItem() }
                    .filter { it.itemType == HOP.ordinal })
            }
        }

        binding.rHoppingConfirmButton.setOnClickListener {
            onItemAdd()
        }

        binding.rHoppingBackButton.setOnClickListener {
            dismiss()
        }

        binding.rHoppingDeleteButton.setOnClickListener {
            onItemDelete()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd() {
        val newTime = binding.rHoppingTimeInput.text.toString()

        if (newTime == "")
            Toast.makeText(context, "Bitte Zeit angeben!", Toast.LENGTH_SHORT).show()
        else {
            adapter.hopping.hoppingTime = newTime
            recipeItem.hoppingList.add(adapter.hopping)
            Toast.makeText(context, "Hopfengabe wurde hinzugefügt!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onItemDelete() {
        val index = recipeItem.hoppingList.count() - 1
        if (index != -1) {
            recipeItem.hoppingList.removeAt(index)
            Toast.makeText(context, "Hofengabe wurde entfernt!", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(context, "Hofengabe nicht vorhanden!", Toast.LENGTH_SHORT).show()
    }
}