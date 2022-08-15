package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogRestBinding
import com.example.brauportv2.model.recipe.Rest
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItem

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
    ): View {
        _binding = FragmentDialogRestBinding.inflate(inflater, container, false)

        binding.restConfirmButton.setOnClickListener {
            val restTemp = binding.restTemp.text.toString()
            val restTime = binding.restTime.text.toString()

            if (restTemp == "" || restTime == "")
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            else {
                recipeItem.restList.add(Rest(restTemp, restTime))
                Toast.makeText(context, R.string.added_rest, Toast.LENGTH_SHORT).show()
            }
        }

        binding.restDeleteButton.setOnClickListener {
            if (recipeItem.restList.isEmpty()) {
                Toast.makeText(context, R.string.rest_not_found, Toast.LENGTH_SHORT).show()
            } else {
                recipeItem.restList.removeAt(recipeItem.restList.count() - 1)
                Toast.makeText(context, R.string.deleted_yeast, Toast.LENGTH_SHORT).show()
            }
        }

        binding.restBackButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}