package com.example.brauportv2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeRecipeButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeFragment()
            findNavController().navigate(action)
        }

        binding.homeStockButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToStockFragment()
            findNavController().navigate(action)
        }

        binding.homeBrewButton.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToBrewFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }
}