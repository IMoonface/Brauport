package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauportv2.databinding.FragmentDialogQuestionBinding

class DialogQuestionFragment(
    private val onDialogQuestionDismiss: (Boolean, Boolean) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogQuestionBinding.inflate(inflater, container, false)

        binding.questionWithButton.setOnClickListener {
            onDialogQuestionDismiss(false, true)
            dismiss()
        }

        binding.questionWithoutButton.setOnClickListener {
            onDialogQuestionDismiss(false, false)
            dismiss()
        }

        binding.questionBackButton.setOnClickListener {
            onDialogQuestionDismiss(true, true)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}