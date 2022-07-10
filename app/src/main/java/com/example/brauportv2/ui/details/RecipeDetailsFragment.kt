package com.example.brauportv2.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.databinding.FragmentRecipeDetailsBinding

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
            val action = RecipeDetailsFragmentDirections
                .actionRecipeDetailsFragmentToDialogMaltsFragment()
            findNavController().navigate(action)
        }

        binding.recipeDetailsHops.setOnClickListener {
            val action = RecipeDetailsFragmentDirections
                .actionRecipeDetailsFragmentToDialogHopsFragment()
            findNavController().navigate(action)
        }

        binding.recipeDetailsYeasts.setOnClickListener {
            val action = RecipeDetailsFragmentDirections
                .actionRecipeDetailsFragmentToDialogYeastsFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}