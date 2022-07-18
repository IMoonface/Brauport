package com.example.brauportv2.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.Recipe
import com.example.brauportv2.model.recipeModel.Recipe.recipeItem
import com.example.brauportv2.model.recipeModel.Recipe.recipeStartConfig
import com.example.brauportv2.ui.dialog.*
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        binding.recipeDetailsMalts.setOnClickListener {
            val dialog = DialogMaltsFragment()
            dialog.show(childFragmentManager, "maltsDialog")
        }

        binding.recipeDetailsHops.setOnClickListener {
            val dialog = DialogHopsFragment()
            dialog.show(childFragmentManager, "hoppingDialog")
        }

        binding.recipeDetailsYeasts.setOnClickListener {
            val dialog = DialogYeastsFragment()
            dialog.show(childFragmentManager, "yeastsDialog")
        }

        binding.recipeDetailsRest.setOnClickListener {
            val dialog = DialogRestFragment()
            dialog.show(childFragmentManager, "restDialog")
        }

        binding.recipeDetailsMainBrew.setOnClickListener {
            val dialog = DialogMainBrewFragment()
            dialog.show(childFragmentManager, "restDialog")
        }

        binding.recipeDetailsSave.setOnClickListener {
            recipeItem.recipeName = binding.recipeDetailsTextInput.text.toString()
            recipeItem.rId = UUID.randomUUID().hashCode()
            //TODO: BUG fixen
            if (recipeItem.recipeName == recipeStartConfig.recipeName ||
                recipeItem.rMaltList == recipeStartConfig.rMaltList ||
                recipeItem.rRest == recipeStartConfig.rRest ||
                recipeItem.rHoppingList == recipeStartConfig.rHoppingList ||
                recipeItem.rYeast == recipeStartConfig.rYeast ||
                recipeItem.rMainBrew == recipeStartConfig.rMainBrew
            )
                Toast.makeText(context, "Bitte alle Attribute setzen!", Toast.LENGTH_SHORT)
                    .show()
            else
                viewModel.addRecipe(recipeItem)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}