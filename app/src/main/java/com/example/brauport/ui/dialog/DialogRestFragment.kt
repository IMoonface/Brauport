package com.example.brauport.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogRestBinding
import com.example.brauport.model.recipe.Rest
import com.example.brauport.ui.RecipeFragment.Companion.recipeItem

class DialogRestFragment : BaseDialogFragment() {

    private var _binding: FragmentDialogRestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRestBinding.inflate(inflater, container, false)

        binding.restConfirmButton.setOnClickListener {
            val restTemp = binding.temperature.text.toString()
            val restTime = binding.timeInput.text.toString()

            if (restTemp == "" || restTime == "")
                Toast.makeText(context, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            else {
                recipeItem.restList.add(Rest(restTemp, restTime))
                Toast.makeText(context, R.string.added_rest, Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteButton.setOnClickListener {
            if (recipeItem.restList.isEmpty()) {
                Toast.makeText(context, R.string.rest_not_found, Toast.LENGTH_SHORT).show()
            } else {
                recipeItem.restList.removeAt(recipeItem.restList.count() - 1)
                Toast.makeText(context, R.string.deleted_rest, Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}