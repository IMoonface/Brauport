package com.example.brauportv2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.databinding.FragmentHomeBinding

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        return binding.root
    }
}