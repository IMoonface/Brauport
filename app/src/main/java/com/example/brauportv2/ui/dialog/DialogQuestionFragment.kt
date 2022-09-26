package com.example.brauportv2.ui.dialog

import android.content.DialogInterface
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
    private var abort = false
    private var subtract = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogQuestionBinding.inflate(inflater, container, false)

        binding.questionWithButton.setOnClickListener {
            dismiss()
        }

        binding.questionWithoutButton.setOnClickListener {
            subtract = false
            dismiss()
        }

        binding.questionBackButton.setOnClickListener {
            abort = true
            dismiss()
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogQuestionDismiss(abort, subtract)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}