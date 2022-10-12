package com.example.brauportv2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.model.stock.StockItemType
import com.example.brauportv2.ui.dialog.*
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.viewModel.RecipeViewModel
import com.example.brauportv2.ui.viewModel.RecipeViewModelFactory
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!
    private var update = false

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication).recipeDatabase.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        update = arguments?.getBoolean("recipeUpdate") ?: false

        binding.recipeDetailsInspectButton.setOnClickListener {
            val dialog = DialogRecipeInspectFragment(
                recipeItem.toBrewHistoryItem(), false
            )
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "recipeInspectDialog")
        }

        binding.recipeDetailsMalts.setOnClickListener {
            val dialog = DialogMaltsFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "maltsDialog")
        }

        binding.recipeDetailsHop.setOnClickListener {
            val dialog = DialogHoppingFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "hopDialog")
        }

        binding.recipeDetailsYeast.setOnClickListener {
            val dialog = DialogYeastFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "yeastDialog")
        }

        binding.recipeDetailsRest.setOnClickListener {
            val dialog = DialogRestFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "restDialog")
        }

        binding.recipeDetailsMainBrew.setOnClickListener {
            val dialog = DialogMainBrewFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "mainBrewDialog")
        }

        binding.recipeDetailsSave.setOnClickListener {
            recipeItem.recipeName = binding.recipeDetailsTextInput.text.toString()

            if (recipeItem.recipeName == "")
                Toast.makeText(context, R.string.enter_name, Toast.LENGTH_SHORT).show()
            else if (update) {
                onItemUpdate(recipeItem)
                Toast.makeText(context, R.string.updated_recipe, Toast.LENGTH_SHORT).show()
                findNavController()
                    .navigate(RecipeDetailsFragmentDirections
                        .actionRecipeDetailsFragmentToRecipeFragment())
            } else {
                recipeItem.rId = UUID.randomUUID().hashCode()

                if (recipeValid())
                    Toast.makeText(context, R.string.set_all_attributes, Toast.LENGTH_SHORT).show()
                else {
                    viewModel.addRecipe(recipeItem)
                    Toast.makeText(context, R.string.created_recipe, Toast.LENGTH_SHORT).show()
                    findNavController()
                        .navigate(RecipeDetailsFragmentDirections
                            .actionRecipeDetailsFragmentToRecipeFragment())
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
            item.rId,
            item.recipeName,
            item.maltList,
            item.restList,
            item.hoppingList,
            item.yeast,
            item.mainBrew
        )
    }

    private fun recipeValid(): Boolean {
        val startYeast = StockItem(0, StockItemType.YEAST.ordinal, "", "")
        val startMainBrew = MainBrew("", "")
        return (recipeItem.recipeName == "" || recipeItem.maltList.isEmpty() ||
                recipeItem.restList.isEmpty() || recipeItem.mainBrew == startMainBrew ||
                recipeItem.hoppingList.isEmpty() || recipeItem.yeast == startYeast)
    }
}