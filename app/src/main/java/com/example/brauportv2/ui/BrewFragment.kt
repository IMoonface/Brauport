package com.example.brauportv2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.ui.HomeFragment.Companion.spinnerItemsList
import com.example.brauportv2.ui.details.BrewDetailsFragment
import com.example.brauportv2.ui.details.BrewDetailsFragment.Companion.stepList
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogInstructionBrewFragment
import com.example.brauportv2.ui.dialog.DialogQuestionFragment
import com.example.brauportv2.ui.viewModel.StockViewModel
import com.example.brauportv2.ui.viewModel.StockViewModelFactory
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private lateinit var chosenRecipe: RecipeItem
    private var stockList = emptyList<StockItem>()
    private var withSubtract = true

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.allStockItems.collect { stockItemDataList ->
                stockList = stockItemDataList.map { it.toStockItem() }
            }
        }

        spinnerItemsList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerOptions)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.brewSpinner.adapter = arrayAdapter

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                chosenRecipe = spinnerItemsList[pos]
                if (viewModel.negativeAmount(chosenRecipe, stockList)) {
                    val dialog = DialogQuestionFragment(
                        this@BrewFragment::onDialogQuestionConfirm,
                        this@BrewFragment::onDialogQuestionAbort
                    )
                    dialog.isCancelable = false
                    dialog.show(childFragmentManager, "questionDialog")
                }
                navigateToBrewDetailsFragment()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.brewInfoButton.setOnClickListener {
            val dialog = DialogInstructionBrewFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "brewInfoDialog")
        }

        binding.brewFinishButton.setOnClickListener {
            if (stepList.isNotEmpty()) {
                val dialog = DialogCookingFragment(
                    false, chosenRecipe.toBrewHistoryItem(), this::onDialogCookingConfirm
                )
                dialog.isCancelable = false
                dialog.show(childFragmentManager, "cookingDialog")
            } else
                Toast.makeText(context, R.string.make_recipe, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateDatabase(item: RecipeItem) {
        item.maltList.forEach { malt ->
            viewModel.updateStock(
                id = malt.id,
                itemType = malt.itemType,
                stockName = malt.stockName,
                stockAmount = viewModel.calcAmount(malt, stockList)
            )
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                viewModel.updateStock(
                    id = hop.id,
                    itemType = hop.itemType,
                    stockName = hop.stockName,
                    stockAmount = viewModel.calcAmount(hop, stockList)
                )
            }
        }

        viewModel.updateStock(
            id = item.yeast.id,
            itemType = item.yeast.itemType,
            stockName = item.yeast.stockName,
            stockAmount = viewModel.calcAmount(item.yeast, stockList)
        )
    }

    private fun onDialogCookingConfirm() {
        if (withSubtract)
                updateDatabase(chosenRecipe)
        findNavController()
            .navigate(BrewFragmentDirections.actionBrewFragmentToBrewHistoryFragment())
    }

    fun onDialogQuestionConfirm(subtract: Boolean) {
        withSubtract = subtract
    }

    fun onDialogQuestionAbort() {
        findNavController().navigate(BrewFragmentDirections.actionBrewFragmentToHomeFragment())
    }

    private fun navigateToBrewDetailsFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.brew_fragment_container, BrewDetailsFragment(chosenRecipe))
            .disallowAddToBackStack()
            .commit()
    }
}