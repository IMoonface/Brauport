package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.databinding.FragmentDialogRestBinding
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItem
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
            val restTemp = binding.restTemp.text.toString()
            val restTime = binding.restTime.text.toString()
            if (restTemp == "" || restTemp == "")
                Toast.makeText(context, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show()
            else {
                recipeItem.restList.add(Rest("$restTemp°C", restTime + "min"))
                Toast.makeText(context, "Rast wurde hinzugefügt", Toast.LENGTH_SHORT).show()
            }
        }

        binding.restDeleteButton.setOnClickListener {
            if (recipeItem.restList.isEmpty()) {
                Toast.makeText(context, "Keine Rast vorhanden", Toast.LENGTH_SHORT).show()
            } else {
                val index = recipeItem.restList.count() - 1
                recipeItem.restList.removeAt(index)
                Toast.makeText(context, "Rast wurde gelöscht", Toast.LENGTH_SHORT).show()
            }
        }

        binding.restAbortButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}