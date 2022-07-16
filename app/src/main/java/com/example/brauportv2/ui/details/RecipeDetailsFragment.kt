package com.example.brauportv2.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding
import com.example.brauportv2.model.StockItemType
import com.example.brauportv2.ui.dialog.DialogHopsFragment
import com.example.brauportv2.ui.dialog.DialogRestFragment
import com.example.brauportv2.ui.dialog.DialogStockFragment
import com.example.brauportv2.ui.dialog.DialogYeastsFragment

class RecipeDetailsFragment : Fragment() {

    private var _binding: FragmentRecipeDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        binding.recipeDetailsMalts.setOnClickListener {
            val dialog = DialogYeastsFragment()
            dialog.show(childFragmentManager, "yeastsDialog")
        }

        binding.recipeDetailsHops.setOnClickListener {
            val dialog = DialogHopsFragment()
            dialog.show(childFragmentManager, "hoppingDialog")
        }

        binding.recipeDetailsYeasts.setOnClickListener {
            val dialog = DialogYeastsFragment()
            dialog.show(childFragmentManager, "yeastsDialog")
        }

        binding.recipeDetailsRest.setOnClickListener {
            val dialog = DialogRestFragment()
            dialog.show(childFragmentManager, "restDialog")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}