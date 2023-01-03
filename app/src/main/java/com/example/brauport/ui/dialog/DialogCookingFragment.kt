package com.example.brauport.ui.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.brauport.BaseApplication
import com.example.brauport.R
import com.example.brauport.databinding.FragmentDialogCookingBinding
import com.example.brauport.model.brewHistory.BrewHistoryItem
import com.example.brauport.ui.viewModel.BrewHistoryViewModel
import com.example.brauport.ui.viewModel.BrewHistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DialogCookingFragment(
    private val update: Boolean,
    private val item: BrewHistoryItem,
    private val onDialogCookingConfirm: () -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogCookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BrewHistoryViewModel by activityViewModels {
        BrewHistoryViewModelFactory(
            (activity?.application as BaseApplication).brewHistoryDatabase.brewHistoryDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogCookingBinding.inflate(inflater, container, false)

        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val actualDate = formatter.format(Calendar.getInstance().time)

        binding.cookingConfirmButton.setOnClickListener {
            val dateOfCompletion = formatter.format(Calendar.getInstance().time)
            val endOfFermentation = binding.cookingText.text.toString()

            if (viewModel.dateIsValid(endOfFermentation, formatter)) {
                val date = formatter.parse(endOfFermentation)
                date?.let {
                    if (date.before(formatter.parse(actualDate)))
                        item.cardColor = Color.GRAY
                    else
                        item.cardColor = 1
                }

                if (!update) {
                    onItemAdd(dateOfCompletion, endOfFermentation)
                    Toast.makeText(context, R.string.finished_recipe, Toast.LENGTH_SHORT).show()
                    onDialogCookingConfirm()
                    dismiss()
                } else {
                    onItemUpdate(endOfFermentation)
                    Toast.makeText(context, R.string.updated_date, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            } else
                Toast.makeText(context, R.string.invalid_date, Toast.LENGTH_SHORT).show()
        }

        binding.cookingAbortButton.setOnClickListener {
            Toast.makeText(context, R.string.aborted_recipe, Toast.LENGTH_SHORT).show()
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
            bId = item.bId,
            bName = item.bName,
            bMaltList = item.bMaltList,
            bRestList = item.bRestList,
            bHoppingList = item.bHoppingList,
            bYeast = item.bYeast,
            bMainBrew = item.bMainBrew,
            bDateOfCompletion = item.bDateOfCompletion,
            bEndOfFermentation = endOfFermentation,
            cardColor = item.cardColor
        )
    }
}