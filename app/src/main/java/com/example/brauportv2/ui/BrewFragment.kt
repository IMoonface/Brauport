package com.example.brauportv2.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.mapper.toBrewHistoryItem
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.model.stock.StockItem
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.dialog.DialogQuestionFragment
import com.example.brauportv2.ui.objects.RecipeDataSource.recipeItemList
import com.example.brauportv2.ui.viewModel.BrewViewModel
import com.example.brauportv2.ui.viewModel.BrewViewModelFactory
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var chosenRecipe: RecipeItem
    private var stockList = emptyList<StockItem>()
    private var milliLeft: Long = 0
    private var milliFromItem: Long = 0
    private var withSubtract = true
    private var startTimer = false
    private val viewModel: BrewViewModel by activityViewModels {
        BrewViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)

        binding.brewRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> stockList = it.map { it.toStockItem() } }
        }

        recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                binding.brewTimerText.text = getString(R.string.click_item)
                chosenRecipe = recipeItemList[pos]
                if (viewModel.proveForNonNegAmount(chosenRecipe, stockList))
                    adapter.submitList(createStringList(chosenRecipe))
                else if (!viewModel.changeInStock) {
                    val dialog = DialogQuestionFragment(this@BrewFragment::onDialogQuestionDismiss)
                    dialog.isCancelable = false
                    dialog.show(childFragmentManager, "questionDialog")
                } else
                    Toast.makeText(context, R.string.change_in_stock_text, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                binding.brewTimerText.text = getString(R.string.create_recipe)
            }
        }

        binding.brewFinishButton.setOnClickListener {
            var finished = false
            adapter.currentList.forEach { finished = it.state }

            if (finished) {
                val dialog = DialogCookingFragment(
                    false,
                    chosenRecipe.toBrewHistoryItem(),
                    this::onDialogCookingDismiss
                )
                dialog.isCancelable = false
                dialog.show(childFragmentManager, "cookingDialog")
            } else
                Toast.makeText(context, R.string.not_all_steps_completed, Toast.LENGTH_SHORT)
                    .show()
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") &&
                binding.brewTimerText.text != getString(R.string.click_item) &&
                binding.brewTimerText.text != getString(R.string.create_recipe) && !startTimer
            ) {
                startTimer = true
                timerStart(milliFromItem)
            } else if (binding.brewTimerStartButton.text.equals(getString(R.string.along))) {
                timerStart(milliLeft)
                binding.brewTimerStartButton.text = "Start"
                binding.brewTimerStopButton.text = "Stop"
                startTimer = true
            } else if (binding.brewTimerStartButton.text.equals("Start") && startTimer)
                Toast.makeText(context, R.string.timer_already_running, Toast.LENGTH_SHORT).show()
        }

        binding.brewTimerStopButton.setOnClickListener {
            countDownTimer?.let {
                if (binding.brewTimerStopButton.text.equals("Stop") &&
                    binding.brewTimerText.text != getString(R.string.click_item)
                ) {
                    it.cancel()
                    binding.brewTimerStartButton.text = getString(R.string.along)
                    binding.brewTimerStopButton.text = "Cancel"
                } else if (binding.brewTimerText.text == getString(R.string.click_item))
                    Toast.makeText(
                        context, R.string.no_time_chosen, Toast.LENGTH_SHORT
                    ).show()
                else {
                    it.cancel()
                    binding.brewTimerText.text = getString(R.string.end)
                    binding.brewTimerStartButton.text = "Start"
                    binding.brewTimerStopButton.text = "Stop"
                    startTimer = false
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun onItemClick(item: BrewItem) {
        if (startTimer)
            Toast.makeText(context, R.string.timer_already_running, Toast.LENGTH_SHORT).show()
        else {
            binding.brewTimerStartButton.text = "Start"
            if (item.brewTime != "") {
                milliFromItem = item.brewTime.toLong() * 60000
                timerStart(milliFromItem)
            }
        }
    }

    fun createStringList(item: RecipeItem): List<BrewItem> {
        val newBrewList = mutableListOf<BrewItem>()

        item.maltList.forEach {
            newBrewList.add(
                BrewItem(it.stockName + " " + it.stockAmount, "", false)
            )
        }

        newBrewList.add(BrewItem(getString(R.string.grinding_malt), "", false))

        newBrewList.add(
            BrewItem(
                getString(R.string.first_brew) + ": " + item.mainBrew.firstBrew,
                "",
                false
            )
        )

        item.restList.forEach {
            newBrewList.add(BrewItem(it.restTemp, it.restTime, false))
        }

        newBrewList.add(
            BrewItem(
                getString(R.string.second_brew) + ": " + item.mainBrew.secondBrew,
                "",
                false
            )
        )

        newBrewList.add(
            BrewItem(
                getString(R.string.remove_malt),
                "",
                false
            )
        )
        newBrewList.add(
            BrewItem(getString(R.string.heat_to_about_temperature),
                "",
                false
            )
        )

        var hoppingListString = ""
        item.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                hoppingListString += hop.stockName + " " + hop.stockAmount + " "
            }
            newBrewList.add(BrewItem(hoppingListString, hopping.hoppingTime, false))
            hoppingListString = ""
        }

        newBrewList.add(BrewItem(getString(R.string.pipeing), "", false))

        newBrewList.add(
            BrewItem(
                getString(R.string.let_it_cool_down),
                "",
                false
            )
        )

        newBrewList.add(
            BrewItem(
                item.yeast.stockName + " " + item.yeast.stockAmount,
                "",
                false
            )
        )

        return newBrewList
    }

    @SuppressLint("SetTextI18n")
    private fun timerStart(timeInMilli: Long) {
        binding.brewTimerText.text = viewModel.minutes(timeInMilli) + ":00"
        if (startTimer) {
            countDownTimer = object : CountDownTimer((timeInMilli), 1000) {
                override fun onTick(untilFinish: Long) {
                    milliLeft = untilFinish
                    binding.brewTimerText.text =
                        viewModel.minutes(untilFinish) + ":" + viewModel.seconds(untilFinish)
                }

                override fun onFinish() {
                    binding.brewTimerText.text = getString(R.string.end)
                    binding.brewTimerStartButton.text = "Start"
                    milliFromItem = 0
                    startTimer = false
                }
            }.start()
        }
    }

    private fun updateDatabase(item: RecipeItem) {
        item.maltList.forEach { malt ->
            viewModel.updateStock(
                malt.id,
                malt.itemType,
                malt.stockName,
                viewModel.calcAmount(malt, stockList)
            )
        }

        item.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                viewModel.updateStock(
                    hop.id,
                    hop.itemType,
                    hop.stockName,
                    viewModel.calcAmount(hop, stockList)
                )
            }
        }

        viewModel.updateStock(
            item.yeast.id,
            item.yeast.itemType,
            item.yeast.stockName,
            viewModel.calcAmount(item.yeast, stockList)
        )
    }

    private fun onDialogCookingDismiss(abort: Boolean) {
        if (abort)
            Toast.makeText(context, R.string.aborted_recipe, Toast.LENGTH_SHORT)
                .show()
        else
            if (withSubtract)
                updateDatabase(chosenRecipe)
    }

    fun onDialogQuestionDismiss(abort: Boolean, subtract: Boolean) {
        if (abort)
            Toast.makeText(context, R.string.choose_another_recipe, Toast.LENGTH_SHORT)
                .show()
        else {
            withSubtract = subtract
            adapter.submitList(createStringList(chosenRecipe))
        }
    }
}