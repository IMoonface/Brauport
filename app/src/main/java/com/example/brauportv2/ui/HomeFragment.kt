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

        binding.mainRecipeButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToRecipeFragment())
        }

        binding.mainStockButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToStockFragment())
        }

        binding.mainBrewButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToBrewFragment())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}