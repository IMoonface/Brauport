package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauportv2.databinding.FragmentDialogInstructionBrewBinding

class DialogInstructionBrewFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogInstructionBrewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogInstructionBrewBinding
            .inflate(inflater, container, false)

        binding.instructionBBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}