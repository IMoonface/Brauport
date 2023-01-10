package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauport.databinding.FragmentDialogQuestionBinding

class DialogQuestionFragment(
    private val onDialogQuestionConfirm: (Boolean) -> Unit,
    private val onDialogQuestionAbort: () -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogQuestionBinding.inflate(inflater, container, false)

        binding.withButton.setOnClickListener {
            onDialogQuestionConfirm(true)
            dismiss()
        }

        binding.withoutButton.setOnClickListener {
            onDialogQuestionConfirm(false)
            dismiss()
        }

        binding.backButton.setOnClickListener {
            onDialogQuestionAbort()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}