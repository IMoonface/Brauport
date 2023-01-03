package com.example.brauport.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.BrewHistoryAdapter
import com.example.brauport.mapper.toBrewHistoryItem
import com.example.brauport.model.brewHistory.BrewHistoryItem
import com.example.brauport.ui.dialog.DialogCookingFragment
import com.example.brauport.ui.dialog.DialogDeleteFragment
import com.example.brauport.ui.dialog.DialogRecipeInspectFragment
import com.example.brauport.ui.viewModel.BrewHistoryViewModel
import com.example.brauport.ui.viewModel.BrewHistoryViewModelFactory
import com.example.brauportv2.databinding.FragmentBrewHistoryBinding
import kotlinx.coroutines.launch

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewHistoryAdapter

    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        adapter = BrewHistoryAdapter(this::onInspectItem, this::onItemClick, this::onDeleteClick)
        binding.brewHistoryRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allBrewHistoryItems.collect { brewHistoryItemDataList ->
                adapter.submitList(brewHistoryItemDataList.map { it.toBrewHistoryItem() })
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onInspectItem(item: BrewHistoryItem) {
        val dialog = DialogRecipeInspectFragment(item, true)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onItemClick(item: BrewHistoryItem) {
        val dialog = DialogCookingFragment(true, item, this::onDialogCookingDismiss)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onDeleteClick(item: BrewHistoryItem) {
        val dialog = DialogDeleteFragment(item, this::onDeleteConfirm)
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "recipeDeleteDialog")
    }

    private fun onDeleteConfirm(item: BrewHistoryItem) {
        viewModel.deleteBrewHistoryItem(item)
    }

    private fun onDialogCookingDismiss() {}
}