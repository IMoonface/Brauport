package com.example.brauport.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brauport.BaseApplication
import com.example.brauport.R
import com.example.brauport.databinding.FragmentRecipeDetailsBinding
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.model.stock.StockItemType
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem
import com.example.brauport.ui.dialog.*
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
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
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        val update = arguments?.getBoolean("recipeUpdate") ?: false

        binding.inspectButton.setOnClickListener {
            val dialog = DialogRecipeInspectFragment(recipeItem)
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "recipeInspectDialog")
        }

        binding.malts.setOnClickListener {
            val dialog = DialogMaltsFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "maltsDialog")
        }

        binding.hopping.setOnClickListener {
            val dialog = DialogHoppingFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "hopDialog")
        }

        binding.yeast.setOnClickListener {
            val dialog = DialogYeastFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "yeastDialog")
        }

        binding.rests.setOnClickListener {
            val dialog = DialogRestFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "restDialog")
        }

        binding.mainBrew.setOnClickListener {
            val dialog = DialogMainBrewFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "mainBrewDialog")
        }

        binding.saveButton.setOnClickListener {
            recipeItem.name = binding.textInput.text.toString()
            if (recipeNotValid())
                Toast.makeText(
                    context,
                    R.string.choose_all_recipe_ingredients,
                    Toast.LENGTH_SHORT
                ).show()
            else {
                if (update) {
                    onItemUpdate(recipeItem)
                    Toast.makeText(context, R.string.updated_recipe, Toast.LENGTH_SHORT).show()

                    findNavController().navigate(
                        RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToRecipeFragment()
                    )
                } else {
                    recipeItem.id = UUID.randomUUID().hashCode()
                    viewModel.addRecipe(recipeItem)
                    Toast.makeText(context, R.string.created_recipe, Toast.LENGTH_SHORT).show()

                    findNavController().navigate(
                        RecipeDetailsFragmentDirections.actionRecipeDetailsFragmentToRecipeFragment()
                    )
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemUpdate(item: RecipeItem) {
        viewModel.updateRecipe(
            id = item.id,
            name = item.name,
            maltList = item.maltList,
            restList = item.restList,
            hoppingList = item.hoppingList,
            yeast = item.yeast,
            mainBrew = item.mainBrew,
            dateOfCompletion = item.dateOfCompletion,
            endOfFermentation = item.endOfFermentation,
            cardColor = item.cardColor,
            isBrewHistoryItem = item.isBrewHistoryItem,
            isRecipeItem = item.isRecipeItem
        )
    }

    private fun recipeNotValid(): Boolean {
        val defaultYeast = StockItem(
            id = 0, itemType = StockItemType.YEAST.ordinal, stockName = "", stockAmount = ""
        )

        val defaultMainBrew = MainBrew(firstBrew = "", secondBrew = "")

        return (recipeItem.name == "" || recipeItem.maltList.isEmpty() ||
                recipeItem.restList.isEmpty() || recipeItem.mainBrew == defaultMainBrew ||
                recipeItem.hoppingList.isEmpty() || recipeItem.yeast == defaultYeast)
    }
}