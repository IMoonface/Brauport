package com.example.brauportv2.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItemList
import com.example.brauportv2.model.recipeModel.RecipeItem

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private var finished = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)
        binding.brewRecyclerView.adapter = adapter

        recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                adapter.submitList(createStringList(recipeItemList[position]))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.brewFinishButton.setOnClickListener {
            adapter.currentList.forEach {
                finished = it.state
            }

            if (finished) {
                Toast.makeText(
                    context,
                    "Rezept abgeschlossen",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else
                Toast.makeText(
                    context,
                    "Noch nicht alle Schritte bestätigt",
                    Toast.LENGTH_SHORT
                ).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onItemClick(brewItem: BrewItem) {
        //TODO: Timer programmieren
    }

    fun createStringList(recipeItem: RecipeItem): List<BrewItem> {
        val newBrewList = emptyList<BrewItem>().toMutableList()
        recipeItem.maltList.forEach {
            newBrewList.add(BrewItem(it.rStockName + " " + it.rStockAmount, "", false))
        }

        newBrewList.add(BrewItem("Malz Schroten", "", false))
        newBrewList.add(BrewItem("Hauptguss: " + recipeItem.mainBrew.firstBrew, "", false))

        //Rasten Frage: Alle auf einmal???

        newBrewList.add(BrewItem("Nachguss: " + recipeItem.mainBrew.secondBrew, "", false))
        newBrewList.add(BrewItem("Malz entnehmen", "", false))
        newBrewList.add(BrewItem("Auf etwa Temperatur erhitzen", "", false))
        recipeItem.hoppingList.forEach {
            newBrewList.add(
                BrewItem(
                    it.hoppingName + " " + it.hoppingAmount,
                    it.hoppingTime,
                    false
                )
            )
        }

        newBrewList.add(BrewItem("Schlauchen", "", false))
        newBrewList.add(BrewItem("Abkühlen lassen", "", false))
        newBrewList.add(
            BrewItem(
                recipeItem.yeast.rStockName + " " + recipeItem.yeast.rStockAmount,
                "",
                false
            )
        )

        newBrewList.add(BrewItem("Abfüllen evt. Flaschengärung", "", false))
        newBrewList.add(BrewItem("Genießen", "", false))

        return newBrewList
    }
}