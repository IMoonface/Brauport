package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauport.databinding.FragmentDialogInstructionRecipeBinding

class DialogInstructionRecipeFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogInstructionRecipeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogInstructionRecipeBinding
            .inflate(inflater, container, false)

        binding.instructionRBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}