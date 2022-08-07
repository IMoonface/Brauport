package com.example.brauportv2.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogRecipeInspectBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem

class DialogRecipeInspectFragment(
    private val recipe: BrewHistoryItem,
    private val fromBrewHistory: Boolean
) : DialogFragment() {

    private var _binding: FragmentDialogRecipeInspectBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        _binding = FragmentDialogRecipeInspectBinding.inflate(inflater, container, false)

        val maltNameList = mutableListOf<String>()
        val restNameList = mutableListOf<String>()
        var hopsNameList = mutableListOf<String>()
        val hoppingNameList = mutableListOf<String>()

        recipe.bMaltList.forEach {
            maltNameList.add(it.stockName + " " + it.stockAmount)
        }

        recipe.bRestList.forEach {
            restNameList.add(it.restTemp + " für " + it.restTime + "min")
        }

        recipe.bHoppingList.forEach { hopping ->
            hopping.hopsList.forEach {
                hopsNameList.add(it.stockName + " " + it.stockAmount)
            }
            hoppingNameList.add(hopsNameList.toString() + " " + hopping.hoppingTime + "min")
            hopsNameList = mutableListOf()
        }

        binding.inspectRecipeName.text = recipe.bName

        binding.inspectMaltList.text = "Malze: $maltNameList"

        binding.inspectRestList.text = "Rasten: $restNameList"

        binding.inspectHoppingList.text = "Hopfengaben: $hoppingNameList"

        binding.inspectYeast.text = "Hefe: " + recipe.bYeast.stockName + " " +
                recipe.bYeast.stockAmount

        binding.inspectMainBrew.text = "Guss: Hauptguss mit " + recipe.bMainBrew.firstBrew +
                " und Nachguss mit " + recipe.bMainBrew.secondBrew

        if (fromBrewHistory) {
            binding.inspectDateOfComp.text = "Fertigstellungsdatum: " + recipe.bDateOfCompletion
            binding.inspectEndOfFerm.text = "Ende der Gärung: " + recipe.bEndOfFermentation
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