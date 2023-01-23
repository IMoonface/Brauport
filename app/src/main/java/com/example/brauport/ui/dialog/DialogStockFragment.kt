package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.brauport.BaseApplication
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogStockBinding
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType
import com.example.brauport.ui.HomeFragment
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory

class DialogStockFragment(
    private val item: StockItem,
    private val onItemAdd: (StockItem) -> Unit,
    private val onItemUpdate: (StockItem) -> Unit,
    private val update: Boolean
) : BaseDialogFragment() {

    private var _binding: FragmentDialogStockBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication).recipeDatabase.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogStockBinding.inflate(inflater, container, false)

        binding.confirmButton.setOnClickListener {
            val stockName = binding.itemName.text.toString()
            val stockAmount = binding.itemAmount.text.toString()

            if (stockName == "" || stockAmount == "") {
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            } else {
                item.stockName = stockName
                item.stockAmount = stockAmount
                if (update) {
                    updateRecipes(item)
                    onItemUpdate(item)
                } else
                    onItemAdd(item)
                dismiss()
            }
        }

        binding.abortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun updateRecipes(item: StockItem) {
        HomeFragment.spinnerItemList.forEach { recipeItem ->
            if (item.itemType == StockItemType.MALT.ordinal)
                recipeItem.maltList.forEach { stockItem ->
                    if (differentFromRecipeStock(stockItem, item)) {
                        stockItem.stockName = item.stockName
                        stockItem.stockAmount = item.stockAmount
                    }
                }

            if (item.itemType == StockItemType.HOP.ordinal)
                recipeItem.hoppingList.forEach { hopping ->
                    hopping.hopList.forEach { stockItem ->
                        if (differentFromRecipeStock(stockItem, item)) {
                            stockItem.stockName = item.stockName
                            stockItem.stockAmount = item.stockAmount
                        }
                    }
                }

            if (item.itemType == StockItemType.YEAST.ordinal)
                if (differentFromRecipeStock(recipeItem.yeast, item)) {
                    recipeItem.yeast.stockName = item.stockName
                    recipeItem.yeast.stockAmount = item.stockAmount
                }

            viewModel.updateRecipe(
                id = recipeItem.id,
                name = recipeItem.name,
                maltList = recipeItem.maltList,
                restList = recipeItem.restList,
                hoppingList = recipeItem.hoppingList,
                yeast = recipeItem.yeast,
                mainBrew = recipeItem.mainBrew
            )
        }
    }

    private fun differentFromRecipeStock(recipeStock: StockItem, item: StockItem): Boolean {
        return recipeStock.id == item.id &&
                (recipeStock.stockName != item.stockName ||
                        recipeStock.stockAmount != item.stockAmount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}