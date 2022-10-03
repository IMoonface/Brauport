package com.example.brauportv2.ui.dialog

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentDialogCookingBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModel
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DialogCookingFragment(
    private val update: Boolean,
    private val item: BrewHistoryItem,
    private val onDialogCookingDismiss: (Boolean) -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogCookingBinding? = null
    private val binding get() = _binding!!
    private val itemId = item.bId

    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogCookingBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getSharedPreferences("myPref", MODE_PRIVATE)

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.cookingConfirmButton.setOnClickListener {
            val dateOfCompletion = formatter.format(Calendar.getInstance().time)
            val endOfFermentation = binding.cookingText.text.toString()

            if (viewModel.dateIsValid(endOfFermentation, formatter) && !update) {
                onItemAdd(dateOfCompletion, endOfFermentation)
                Toast.makeText(context, R.string.finished_recipe, Toast.LENGTH_SHORT).show()

                sharedPref?.let {
                    it.edit().apply {
                        putInt("lastRecipeId", itemId)
                        apply()
                    }
                }

                onDialogCookingDismiss(false)

                dismiss()
            } else if (viewModel.dateIsValid(endOfFermentation, formatter) && update) {
                onItemUpdate(endOfFermentation)
                Toast.makeText(context, R.string.updated_date, Toast.LENGTH_SHORT).show()
                dismiss()
            } else
                Toast.makeText(context, R.string.invalid_date, Toast.LENGTH_SHORT).show()
        }

        binding.cookingAbortButton.setOnClickListener {
            onDialogCookingDismiss(true)
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemAdd(dateOfCompletion: String, endOfFermentation: String) {
        item.bId = UUID.randomUUID().hashCode()
        item.bDateOfCompletion = dateOfCompletion
        item.bEndOfFermentation = endOfFermentation
        viewModel.addBrewHistoryItem(item)
    }

    private fun onItemUpdate(endOfFermentation: String) {
        viewModel.updateBrewHistoryItem(
            item.bId,
            item.bName,
            item.bMaltList,
            item.bRestList,
            item.bHoppingList,
            item.bYeast,
            item.bMainBrew,
            item.bDateOfCompletion,
            endOfFermentation
        )
    }
}