package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauportv2.databinding.FragmentDialogInstructionStockBinding

class DialogInstructionStockFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogInstructionStockBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogInstructionStockBinding
            .inflate(inflater, container, false)

        binding.instructionSBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}