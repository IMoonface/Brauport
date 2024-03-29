package com.example.brauport.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogMainBrewBinding
import com.example.brauport.model.recipe.MainBrew
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem

class DialogMainBrewFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogMainBrewBinding? = null
    private val binding get() = _binding!!

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogMainBrewBinding.inflate(inflater, container, false)

        binding.confirmButton.setOnClickListener {
            val firstBrew = binding.firstPour.text.toString()
            val secondBrew = binding.secondPour.text.toString()

            if (firstBrew == "" || secondBrew == "")
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            else {
                recipeItem.mainBrew = MainBrew(firstBrew + "ml", secondBrew + "ml")
                Toast.makeText(context, R.string.added_main_brew, Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        binding.abortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}