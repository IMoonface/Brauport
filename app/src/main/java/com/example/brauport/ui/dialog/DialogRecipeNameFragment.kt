package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.brauport.BaseApplication
import com.example.brauport.databinding.FragmentDialogRecipeNameBinding
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory

class DialogRecipeNameFragment(private val item: RecipeItem) : BaseDialogFragment() {

    private var _binding: FragmentDialogRecipeNameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication).recipeDatabase.recipeDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRecipeNameBinding.inflate(inflater, container, false)

        binding.confirmButton.setOnClickListener {
            viewModel.updateRecipe(
                item.rId,
                binding.recipeName.text.toString(),
                item.maltList,
                item.restList,
                item.hoppingList,
                item.yeast,
                item.mainBrew
            )

            dismiss()
        }

        binding.abortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}