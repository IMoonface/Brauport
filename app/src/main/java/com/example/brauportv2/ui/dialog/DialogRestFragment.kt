package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogRestBinding
import com.example.brauportv2.model.recipeModel.Recipe
import com.example.brauportv2.model.recipeModel.Rest

class DialogRestFragment : DialogFragment() {

    private var _binding: FragmentDialogRestBinding? = null
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
        // Inflate the layout for this fragment
        _binding = FragmentDialogRestBinding.inflate(inflater, container, false)

        binding.restConfirmButton.setOnClickListener {
            val itemTemp = binding.restTemp.text.toString() + "°C"
            val itemTime = binding.restTime.text.toString() + "min"

            Recipe.recipeItem.rRest = Rest(itemTemp, itemTime)

            dismiss()
        }

        binding.restAbortDialog.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}