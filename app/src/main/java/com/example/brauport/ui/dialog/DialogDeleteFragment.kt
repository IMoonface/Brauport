package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogDeleteBinding
import com.example.brauport.model.recipe.RecipeItem

class DialogDeleteFragment(
    private val item: RecipeItem,
    private val fromBrewHistory: Boolean,
    private val onDeleteConfirm: (RecipeItem, Boolean) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogDeleteBinding? = null
    private val binding get() = _binding!!
    private var brewHistoryItemDelete = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)

        binding.yesButton.setOnClickListener {
            onDeleteConfirm(item, brewHistoryItemDelete)
            dismiss()
        }

        binding.noButton.setOnClickListener {
            dismiss()
        }

        if (fromBrewHistory && item.isRecipeItem)
            binding.checkBox.text = getString(R.string.keep_recipe_data)
        else if (!item.isRecipeItem || !item.isBrewHistoryItem) {
            binding.checkBox.isEnabled = false
            binding.checkBox.alpha = 0.5F
        }

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            brewHistoryItemDelete = isChecked
        }

        return binding.root
    }
}