package com.example.brauportv2.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauportv2.databinding.FragmentDialogDeleteBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem

class DialogDeleteFragment(
    private val item: BrewHistoryItem,
    private val onDeleteConfirm: (BrewHistoryItem) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)

        binding.deleteYesButton.setOnClickListener {
            onDeleteConfirm(item)
            dismiss()
        }

        binding.deleteNoButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}