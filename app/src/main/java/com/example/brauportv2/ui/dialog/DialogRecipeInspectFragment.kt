package com.example.brauportv2.ui.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogRecipeInspectBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem

class DialogRecipeInspectFragment(
    private val item: BrewHistoryItem,
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
        _binding = FragmentDialogRecipeInspectBinding
            .inflate(inflater, container, false)

        val maltNameList = mutableListOf<String>()
        val restNameList = mutableListOf<String>()
        var hopsNameList = mutableListOf<String>()
        val hoppingNameList = mutableListOf<String>()

        item.bMaltList.forEach {
            maltNameList.add(it.stockName + " " + it.stockAmount)
        }

        item.bRestList.forEach {
            restNameList.add(it.restTemp + getString(R.string.unit_of_measurement_temp) + " " + getString(R.string.for_text) + " " + it.restTime + "min")
        }

        item.bHoppingList.forEach { hopping ->
            hopping.hopsList.forEach {
                hopsNameList.add(it.stockName + " " + it.stockAmount)
            }
            hoppingNameList.add(hopsNameList.toString() + " " + hopping.hoppingTime + "min")
            hopsNameList = mutableListOf()
        }

        binding.inspectRecipeName.text = item.bName

        binding.inspectMaltList.text = getString(R.string.malts) + ": " + maltNameList

        binding.inspectRestList.text = getString(R.string.rests) + ": " + restNameList

        binding.inspectHoppingList.text = getString(R.string.hopping) + ": " + hoppingNameList

        binding.inspectYeast.text = getString(R.string.yeast) + ": " + item.bYeast.stockName +
                    " " + item.bYeast.stockAmount

        binding.inspectMainBrew.text =
            getString(R.string.first_brew_with) + " " + item.bMainBrew.firstBrew +
                    " " + getString(R.string.second_brew_with) + " " + item.bMainBrew.secondBrew

        if (fromBrewHistory) {
            binding.inspectDateOfComp.text =
                getString(R.string.date_of_completion) + " " + item.bDateOfCompletion
            binding.inspectEndOfFerm.text =
                getString(R.string.inspect_end_of_fermentation) + " " + item.bEndOfFermentation
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