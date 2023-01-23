package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauport.databinding.FragmentDialogDeleteBinding
import com.example.brauport.model.recipe.RecipeItem

class DialogDeleteFragment(
    private val item: RecipeItem,
    private val onDeleteConfirm: (RecipeItem) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogDeleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)

        binding.yesButton.setOnClickListener {
            onDeleteConfirm(item)
            dismiss()
        }

        binding.noButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}