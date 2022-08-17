package com.example.brauportv2.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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
) : DialogFragment() {

    private var _binding: FragmentDialogCookingBinding? = null
    private val binding get() = _binding!!
    private var abort = false

    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication)
                .brewHistoryDatabase.brewHistoryDao()
        )
    }

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
        _binding = FragmentDialogCookingBinding.inflate(inflater, container, false)

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        binding.cookingConfirmButton.setOnClickListener {
            val dateOfCompletion = formatter.format(Calendar.getInstance().time)
            val endOfFermentation = binding.cookingDate.text.toString()

            abort = false

            if (viewModel.dateIsValid(endOfFermentation, formatter) && !update) {
                onItemAdd(dateOfCompletion, endOfFermentation)
                Toast.makeText(context, R.string.finished_recipe, Toast.LENGTH_SHORT).show()
                dismiss()
            } else if (viewModel.dateIsValid(endOfFermentation, formatter) && update) {
                onItemUpdate(endOfFermentation)
                Toast.makeText(context, R.string.updated_date, Toast.LENGTH_SHORT).show()
                dismiss()
            } else
                Toast.makeText(context, R.string.invalid_date, Toast.LENGTH_SHORT).show()
        }

        binding.cookingAbortButton.setOnClickListener {
            abort = true
            dismiss()
        }

        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogCookingDismiss(abort)
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