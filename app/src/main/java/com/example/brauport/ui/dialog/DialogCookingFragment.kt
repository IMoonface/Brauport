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
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.ui.viewModel.RecipeViewModel
import com.example.brauport.ui.viewModel.RecipeViewModelFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DialogCookingFragment(
    private val update: Boolean,
    private val item: RecipeItem,
    private val onDialogCookingConfirm: () -> Unit
) : BaseDialogFragment() {

    private var _binding: FragmentDialogCookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by activityViewModels {
        RecipeViewModelFactory(
            (activity?.application as BaseApplication).recipeDatabase.recipeDao()
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

            if (dateIsValid(endOfFermentation, formatter)) {
                val date = formatter.parse(endOfFermentation)
                date?.let {
                    if (date.before(formatter.parse(actualDate)))
                        item.cardColor = Color.GRAY
                    else
                        item.cardColor = 1
                }

                if (!update) {
                    item.isBrewHistoryItem = true
                    onItemUpdate(dateOfCompletion, endOfFermentation)
                    Toast.makeText(context, R.string.finished_recipe, Toast.LENGTH_SHORT).show()
                    onDialogCookingConfirm()
                    dismiss()
                } else {
                    onItemUpdate(dateOfCompletion, endOfFermentation)
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

    private fun onItemUpdate(dateOfCompletion: String, endOfFermentation: String) {
        val newDateOfCompletion = if (update)
            item.dateOfCompletion
        else
            dateOfCompletion

        viewModel.updateRecipe(
            id = item.id,
            name = item.name,
            maltList = item.maltList,
            restList = item.restList,
            hoppingList = item.hoppingList,
            yeast = item.yeast,
            mainBrew = item.mainBrew,
            dateOfCompletion = newDateOfCompletion,
            endOfFermentation = endOfFermentation,
            cardColor = item.cardColor,
            isBrewHistoryItem = item.isBrewHistoryItem,
            isRecipeItem = item.isRecipeItem
        )
    }

    private fun dateIsValid(endOfFermentation: String, formatter: SimpleDateFormat): Boolean {
        try {
            formatter.parse(endOfFermentation)
        } catch (e: ParseException) {
            return false
        }
        return true
    }
}