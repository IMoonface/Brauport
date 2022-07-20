package com.example.brauportv2.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItemList
import com.example.brauportv2.model.recipeModel.RecipeItem

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var spinnerOptions : MutableList<String> = mutableListOf()
    private var testArr = listOf("Malz 1-n", "Malz Schroten", "Hauptguss", "Erste Rast", "Nachguss",
        "Malz entnehmen", "Auf etwa Temperatur erhitzen", "Hopfengabe 1-n", "Schlauchen",
        "Abkühlen lassen", "Hefe", "Abfüllen evt. Flaschengärung", "Genießen")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter()
        binding.brewRecyclerView.adapter = adapter
        adapter.submitList(testArr)
        Log.i("Liste", testArr.toString())

        recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                createStringList(recipeItemList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun createStringList(recipeItem: RecipeItem) {
        Log.i("hey", recipeItem.toString())
    }
}