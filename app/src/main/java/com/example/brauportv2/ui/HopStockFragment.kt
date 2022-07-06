package com.example.brauportv2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentHopStockBinding
import com.example.brauportv2.databinding.FragmentMaltStockBinding

class HopsStockFragment : Fragment() {

    private var _binding: FragmentHopStockBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHopStockBinding.inflate(inflater, container, false)
        binding.hopNextButton.setOnClickListener {
            val action = HopsStockFragmentDirections.actionHopsStockFragmentToYeastStockFragment()
            findNavController().navigate(action)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}