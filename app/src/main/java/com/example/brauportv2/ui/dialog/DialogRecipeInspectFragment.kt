package com.example.brauportv2.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogRecipeInspectBinding
import com.example.brauportv2.model.recipeModel.RecipeItem

class DialogRecipeInspectFragment(
    private val recipe: RecipeItem,
    private val fromBrewHistory: Boolean
) : DialogFragment() {

    private var _binding: FragmentDialogRecipeInspectBinding? = null
    private val binding get() = _binding!!
    private var maltNameList = mutableListOf<String>()
    private var restNameList = mutableListOf<String>()
    private var hopsNameList = mutableListOf<String>()
    private var hoppingNameList = mutableListOf<String>()

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogRecipeInspectBinding.inflate(inflater, container, false)

        recipe.maltList.forEach {
            maltNameList.add(it.stockName + " " + it.stockAmount + "g")
        }

        recipe.restList.forEach {
            restNameList.add(it.restTime + " " + it.restTemp)
        }

        recipe.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach {
                hopsNameList.add(it.stockName + " " + it.stockAmount + "g")
            }
            hoppingNameList.add(hopsNameList.toString() + " " + hopping.hoppingTime + "min")
        }

        binding.inspectRecipeName.text = recipe.recipeName

        binding.inspectMaltList.text = "Malze: $maltNameList"

        binding.inspectRestList.text = "Rasten: $restNameList"

        binding.inspectHoppingList.text = "Hopfengaben: $hoppingNameList"

        binding.inspectYeast.text = "Hefe: " +
                recipe.yeast.stockName +
                " " +
                recipe.yeast.stockAmount +
                "g"

        binding.inspectMainBrew.text = "Guss: Hauptguss mit " +
                recipe.mainBrew.firstBrew +
                " und Nachguss mit " +
                recipe.mainBrew.secondBrew

        if (fromBrewHistory) {
            binding.inspectDateOfComp.text = "Fertigstellungsdatum: " + recipe.dateOfCompletion
            binding.inspectEndOfFerm.text = "Ende der Gärung: " + recipe.endOfFermentation
        }

        binding.inspectBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}