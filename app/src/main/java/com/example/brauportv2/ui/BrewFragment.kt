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
import com.example.brauportv2.ui.details.BrewDetailsFragment
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogInstructionBrewFragment
import com.example.brauportv2.ui.dialog.DialogQuestionFragment
import com.example.brauportv2.ui.objects.RecipeDataSource.itemList
import com.example.brauportv2.ui.viewModel.BrewViewModel
import com.example.brauportv2.ui.viewModel.BrewViewModelFactory
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private lateinit var chosenRecipe: RecipeItem
    private var stockList = emptyList<StockItem>()
    private var withSubtract = true

    private val viewModel: BrewViewModel by activityViewModels {
        BrewViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> stockList = it.map { it.toStockItem() } }
        }

        itemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                chosenRecipe = itemList[pos]
                if (viewModel.proveForNonNegAmount(chosenRecipe, stockList))
                    childFragmentManager.beginTransaction()
                        .replace(R.id.brew_fragment_container, BrewDetailsFragment(chosenRecipe))
                        .disallowAddToBackStack()
                        .commit()
                else if (!viewModel.changeInStock) {
                    val dialog = DialogQuestionFragment(this@BrewFragment::onDialogQuestionDismiss)
                    dialog.isCancelable = false
                    dialog.show(childFragmentManager, "questionDialog")
                } else
                    Toast.makeText(context, R.string.change_in_stock_text, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.brewInfoButton.setOnClickListener {
            val dialog = DialogInstructionBrewFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "brewInfoDialog")
        }

        binding.brewFinishButton.setOnClickListener {
            val dialog = DialogCookingFragment(
                false, chosenRecipe.toBrewHistoryItem(), this::onDialogCookingDismiss
            )
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "cookingDialog")
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
                malt.id, malt.itemType, malt.stockName, viewModel.calcAmount(malt, stockList)
            )
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                viewModel.updateStock(
                    hop.id,
                    hop.itemType,
                    hop.stockName,
                    viewModel.calcAmount(hop, stockList)
                )
            }
        }

        viewModel.updateStock(
            item.yeast.id,
            item.yeast.itemType,
            item.yeast.stockName,
            viewModel.calcAmount(item.yeast, stockList)
        )
    }

    private fun onDialogCookingDismiss(abort: Boolean) {
        if (abort)
            Toast.makeText(context, R.string.aborted_recipe, Toast.LENGTH_SHORT).show()
        else {
            if (withSubtract)
                updateDatabase(chosenRecipe)
            findNavController()
                .navigate(BrewFragmentDirections.actionBrewFragmentToBrewHistoryFragment())
        }
    }

    fun onDialogQuestionDismiss(abort: Boolean, subtract: Boolean) {
        if (abort)
            findNavController().navigate(BrewFragmentDirections.actionBrewFragmentToHomeFragment())
        else {
            withSubtract = subtract
            childFragmentManager.beginTransaction()
                .replace(R.id.brew_fragment_container, BrewDetailsFragment(chosenRecipe))
                .disallowAddToBackStack()
                .commit()
        }
    }
}