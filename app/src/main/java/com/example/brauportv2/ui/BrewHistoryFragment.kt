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
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogRecipeInspectFragment
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class BrewHistoryFragment : Fragment() {

    private var _binding: FragmentBrewHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewHistoryAdapter
    private lateinit var brewHistoryList: List<RecipeItem>

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewHistoryBinding.inflate(inflater, container, false)

        adapter = BrewHistoryAdapter(this::onInspectItem, this::onItemClick)
        binding.brewHistoryRecyclerView.adapter = adapter

        val dateNow = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            .format(Calendar.getInstance().time)

        lifecycleScope.launch {
            viewModel.allRecipeItems.collect { it ->
                brewHistoryList = it.map { it.toRecipeItem() }
                    .filter {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            .parse(it.endOfFermentation)
                            .after(
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    .parse(dateNow)
                            )
                    }
                adapter.submitList(brewHistoryList)
            }
        }

        return binding.root
    }

    private fun onInspectItem(item: RecipeItem) {
        val dialog = DialogRecipeInspectFragment(item, true)
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onItemClick(item: RecipeItem) {
        val dialog = DialogCookingFragment(true, item, this::onDialogCookingDismiss)
        dialog.show(childFragmentManager, "cookingDialog")
    }

    private fun onDialogCookingDismiss(abort: Boolean) {

    }
}