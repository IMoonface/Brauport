package com.example.brauportv2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.RStockItem
import com.example.brauportv2.ui.dialog.DialogHopsFragment
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.viewmodel.RecipeViewModelFactory
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StockViewModel by activityViewModels {
        RecipeViewModelFactory((activity?.application as BaseApplication).recipeDatabase.recipeDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        binding.recipeDetailsMalts.setOnClickListener {

        }

        binding.recipeDetailsHops.setOnClickListener {
            val dialog = DialogHopsFragment()
            dialog.show(childFragmentManager, "hoppingDialog")
        }

        binding.recipeDetailsYeasts.setOnClickListener {

        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}