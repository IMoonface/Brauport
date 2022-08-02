package com.example.brauportv2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.startHoppingList
import com.example.brauportv2.ui.objects.RecipeDataSource.startMainBrew
import com.example.brauportv2.ui.objects.RecipeDataSource.startMaltList
import com.example.brauportv2.ui.objects.RecipeDataSource.startRestList
import com.example.brauportv2.ui.objects.RecipeDataSource.startYeast
import com.example.brauportv2.ui.objects.RecipeDataSource.update
import com.example.brauportv2.ui.dialog.*
import com.example.brauportv2.ui.viewModel.RecipeViewModel
import com.example.brauportv2.ui.viewModel.RecipeViewModelFactory
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
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        binding.recipeDetailsInspectButton.setOnClickListener {
            val dialog = DialogRecipeInspectFragment(recipeItem.toBrewHistoryItem(), false)
            dialog.show(childFragmentManager, "recipeDetailsInspectDialog")
        }

        binding.recipeDetailsMalts.setOnClickListener {
            val dialog = DialogMaltsFragment()
            dialog.show(childFragmentManager, "maltsDialog")
        }

        binding.recipeDetailsHops.setOnClickListener {
            val dialog = DialogHoppingFragment()
            dialog.show(childFragmentManager, "hopsDialog")
        }

        binding.recipeDetailsYeasts.setOnClickListener {
            val dialog = DialogYeastFragment()
            dialog.show(childFragmentManager, "yeastsDialog")
        }

        binding.recipeDetailsRest.setOnClickListener {
            val dialog = DialogRestFragment()
            dialog.show(childFragmentManager, "restDialog")
        }

        binding.recipeDetailsMainBrew.setOnClickListener {
            val dialog = DialogMainBrewFragment()
            dialog.show(childFragmentManager, "mainBrewDialog")
        }

        binding.recipeDetailsSave.setOnClickListener {
            val recipeName = binding.recipeDetailsTextInput.text.toString()

            if (update) {
                if (recipeName == "")
                    Toast.makeText(context, "Bitte Namen eingeben!", Toast.LENGTH_SHORT).show()
                else {
                    recipeItem.recipeName = recipeName
                    viewModel.updateRecipe(
                        recipeItem.rId,
                        recipeItem.recipeName,
                        recipeItem.maltList,
                        recipeItem.restList,
                        recipeItem.hoppingList,
                        recipeItem.yeast,
                        recipeItem.mainBrew
                    )
                }
            } else {
                recipeItem.rId = UUID.randomUUID().hashCode()
                recipeItem.recipeName = recipeName

                if (recipeItem.recipeName == "" || recipeItem.maltList == startMaltList ||
                    recipeItem.restList == startRestList || recipeItem.mainBrew == startMainBrew ||
                    recipeItem.hoppingList == startHoppingList || recipeItem.yeast == startYeast
                )
                    Toast.makeText(context, "Bitte alle Attribute setzen!", Toast.LENGTH_SHORT)
                        .show()
                else {
                    viewModel.addRecipe(recipeItem)
                    Toast.makeText(context, "Rezept wurde erstellt!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}