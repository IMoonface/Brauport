package com.example.brauport.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogRecipeInspectBinding
import com.example.brauport.model.brewHistory.BrewHistoryItem

class DialogRecipeInspectFragment(
    private val item: BrewHistoryItem,
    private val fromBrewHistory: Boolean
) : BaseDialogFragment() {

    private var _binding: FragmentDialogRecipeInspectBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRecipeInspectBinding
            .inflate(inflater, container, false)

        val maltNameList = mutableListOf<String>()
        val restNameList = mutableListOf<String>()
        val hoppingNameList = mutableListOf<String>()

        item.maltList.forEach {
            maltNameList.add(it.name + " " + it.amount + "g")
        }

        item.restList.forEach {
            restNameList.add(
                it.restTemp + getString(R.string.unit_of_measurement_temp) + " " +
                        getString(R.string.for_text) + " " + it.restTime + "min"
            )
        }

        item.hoppingList.forEach { hopping ->
            val hopsNameList = mutableListOf<String>()
            hopping.hopList.forEach {
                hopsNameList.add(it.name + " " + it.amount + "g")
            }
            hoppingNameList.add(hopsNameList.toString() + " " + hopping.hoppingTime + "min")
        }

        binding.recipeName.text = item.name

        binding.malts.text = getString(R.string.malts) + ": " + maltNameList

        binding.rests.text = getString(R.string.rest) + ": " + restNameList

        binding.hopping.text = getString(R.string.hopping) + ": " + hoppingNameList

        binding.yeast.text = getString(R.string.yeast) + ": " + item.yeast.name +
                " " + item.yeast.amount

        binding.mainBrew.text =
            getString(R.string.first_brew_with) + " " + item.mainBrew.firstBrew + " " +
                    getString(R.string.second_brew_with) + " " + item.mainBrew.secondBrew

        if (fromBrewHistory) {
            binding.dateOfCompletion.text =
                getString(R.string.date_of_completion) + " " + item.dateOfCompletion
            binding.endOfFermentation.text =
                getString(R.string.inspect_end_of_fermentation) + " " + item.endOfFermentation
        }

        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}