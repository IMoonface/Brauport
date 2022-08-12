package com.example.brauportv2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.adapter.BrewHistoryAdapter
import com.example.brauportv2.databinding.FragmentBrewHistoryBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogRecipeInspectFragment
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModel
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewHistoryAdapter
    private lateinit var brewHistoryList: List<BrewHistoryItem>
    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        adapter = BrewHistoryAdapter(this::onInspectItem, this::onItemClick)
        binding.brewHistoryRecyclerView.adapter = adapter

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val dateOfCompletion = formatter.format(Calendar.getInstance().time)

        lifecycleScope.launch {
            viewModel.allBrewHistoryItems.collect { it ->
                brewHistoryList = it.map { it.toBrewHistoryItem() }
                brewHistoryList.forEach {
                    if (formatter.parse(it.bEndOfFermentation)
                            .before(formatter.parse(dateOfCompletion))
                    )
                        viewModel.deleteRecipe(it)
                }
                adapter.submitList(brewHistoryList)
            }
        }

        return binding.root
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

    private fun onDialogCookingDismiss(abort: Boolean) {}
}