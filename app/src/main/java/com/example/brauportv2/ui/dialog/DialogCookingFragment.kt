package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentDialogCookingBinding
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory

class DialogCookingFragment(
    private val recipe: RecipeItem
) : DialogFragment() {

    private var _binding: FragmentDialogCookingBinding? = null
    private val binding get() = _binding!!
    var abort = false
    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDialogCookingBinding.inflate(inflater, container, false)

        binding.brewHistoryConfirmButton.setOnClickListener {
            //TODO: Datum abfragen
            val dateOfCompletion = "1"
            val endOfFermentation = binding.brewHistoryDate.text.toString()

            if (dateOfCompletion != "" && endOfFermentation != "") {
                onItemAdd(dateOfCompletion, endOfFermentation)
                Toast.makeText(context, "Rezept abgeschlossen", Toast.LENGTH_SHORT).show()
                abort = false
                dismiss()
            } else
                Toast.makeText(context, "Bitte Datum angeben!", Toast.LENGTH_SHORT).show()
        }

        binding.brewHistoryAbortButton.setOnClickListener {
            abort = true
            dismiss()
        }

        return binding.root
    }

    private fun onItemAdd(dateOfCompletion: String, endOfFermentation: String) {
        viewModel.updateRecipe(
            recipe.rId,
            recipe.recipeName,
            recipe.maltList,
            recipe.restList,
            recipe.hoppingList,
            recipe.yeast,
            recipe.mainBrew,
            dateOfCompletion,
            endOfFermentation
        )
    }
}