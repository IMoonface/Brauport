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
import com.example.brauportv2.databinding.FragmentDialogCookingBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModel
import com.example.brauportv2.ui.viewModel.BrewHistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DialogCookingFragment(
    private val update: Boolean,
    private val brewHistoryItem: BrewHistoryItem,
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
    ): View? {
        _binding = FragmentDialogCookingBinding.inflate(inflater, container, false)

        binding.cookingConfirmButton.setOnClickListener {
            val dateOfCompletion = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Calendar.getInstance().time)
            val endOfFermentation = binding.cookingDate.text.toString()

            abort = false

            if (endOfFermentation != "" && !update) {
                onItemAdd(dateOfCompletion, endOfFermentation)
                Toast.makeText(context, "Rezept abgeschlossen!", Toast.LENGTH_SHORT).show()
                dismiss()
            } else if (endOfFermentation != "" && update) {
                onItemUpdate(endOfFermentation)
                Toast.makeText(context, "Datum aktualisiert!", Toast.LENGTH_SHORT).show()
                dismiss()
            } else
                Toast.makeText(context, "Bitte Datum angeben!", Toast.LENGTH_SHORT).show()
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
        //kann man eventuell weglassen
        brewHistoryItem.bId = UUID.randomUUID().hashCode()
        brewHistoryItem.bDateOfCompletion = dateOfCompletion
        brewHistoryItem.bEndOfFermentation = endOfFermentation

        viewModel.addBrewHistoryItem(brewHistoryItem)
    }

    private fun onItemUpdate(endOfFermentation: String) {
        viewModel.updateBrewHistoryItem(
            brewHistoryItem.bId,
            brewHistoryItem.bName,
            brewHistoryItem.bMaltList,
            brewHistoryItem.bRestList,
            brewHistoryItem.bHoppingList,
            brewHistoryItem.bYeast,
            brewHistoryItem.bMainBrew,
            brewHistoryItem.bDateOfCompletion,
            endOfFermentation
        )
    }
}