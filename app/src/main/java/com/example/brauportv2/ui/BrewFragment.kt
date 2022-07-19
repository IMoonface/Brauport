package com.example.brauportv2.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.mapper.toRecipeItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItemList
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.viewmodel.RecipeViewModel
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinnerOptions : MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        RecipeDataSource.recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                createStringList(recipeItemList[position])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

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