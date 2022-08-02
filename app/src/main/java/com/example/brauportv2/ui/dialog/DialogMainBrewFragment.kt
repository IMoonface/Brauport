package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogMainBrewBinding
import com.example.brauportv2.model.recipe.MainBrew
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem

class DialogMainBrewFragment : DialogFragment() {

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogMainBrewBinding.inflate(inflater, container, false)

        binding.mainBrewConfirmButton.setOnClickListener {
            val firstBrew = binding.mainBrewFirst.text.toString()
            val secondBrew = binding.mainBrewSecond.text.toString()

            if (firstBrew == "" || secondBrew == "")
                Toast.makeText(context, "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT)
                    .show()
            else {
                recipeItem.mainBrew = MainBrew(firstBrew + "ml", secondBrew + "ml")
                Toast.makeText(context, "Guss wurde hinzugefügt!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }

        binding.mainBrewAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}