package com.example.brauport.ui.dialog

import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.brauport.databinding.FragmentDialogInstructionStockBinding

class DialogInstructionStockFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogInstructionStockBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogInstructionStockBinding
            .inflate(inflater, container, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.description.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }

        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}