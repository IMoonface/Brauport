package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogWarningBinding

class DialogWarningFragment : DialogFragment() {

    private var _binding: FragmentDialogWarningBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogWarningBinding.inflate(inflater, container, false)

        binding.warningBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}