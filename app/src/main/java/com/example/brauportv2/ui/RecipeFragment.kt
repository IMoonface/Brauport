package com.example.brauportv2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentHomeBinding
import com.example.brauportv2.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.recipeAddButton.setOnClickListener {
            //TODO: Dialog öffnen
        }
        return binding.root
    }

    //TODO: Funktion an ListAdapter übergeben
    private fun onRecipeClick() {
        val action = RecipeFragmentDirections.actionRecipeFragmentToRecipeDetailsFragment()
        findNavController().navigate(action)
    }
}