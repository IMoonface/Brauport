package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogDeleteBinding
import com.example.brauportv2.model.recipe.RecipeItem

class DialogDeleteFragment(
    private val recipeItem: RecipeItem,
    private val onDialogDeleteDismiss: (Boolean, RecipeItem) -> Unit
) : DialogFragment() {

    private var _binding: FragmentDialogDeleteBinding? = null
    private val binding get() = _binding!!
    private var delete = false

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)

        binding.deleteYesButton.setOnClickListener {
            delete = true
            dismiss()
        }

        binding.deleteNoButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogDeleteDismiss(delete, recipeItem)
    }
}