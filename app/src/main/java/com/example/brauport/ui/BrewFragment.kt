package com.example.brauport.ui

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
import androidx.navigation.fragment.findNavController
import com.example.brauport.BaseApplication
import com.example.brauport.adapter.BrewAdapter
import com.example.brauport.mapper.toBrewHistoryItem
import com.example.brauport.mapper.toStockItem
import com.example.brauport.model.brew.StepItem
import com.example.brauport.model.recipe.RecipeItem
import com.example.brauport.model.stock.StockItem
import com.example.brauport.ui.HomeFragment.Companion.spinnerItemList
import com.example.brauport.ui.dialog.DialogCookingFragment
import com.example.brauport.ui.dialog.DialogInstructionBrewFragment
import com.example.brauport.ui.dialog.DialogQuestionFragment
import com.example.brauport.ui.viewModel.StockViewModel
import com.example.brauport.ui.viewModel.StockViewModelFactory
import com.example.brauportv2.R
import com.example.brauportv2.databinding.FragmentBrewBinding
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var countDownTimer: CountDownTimer? = null
    private var milliFromItem: Long = 0
    private var milliLeft: Long = 0
    private var startTimer = false
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private lateinit var chosenRecipe: RecipeItem
    private var stockList = emptyList<StockItem>()
    private var stepList = emptyList<StepItem>()
    private var withSubtract = true

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)
        binding.brewRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { stockItemDataList ->
                stockList = stockItemDataList.map { it.toStockItem() }
            }
        }

        spinnerItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, spinnerOptions)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.brewSpinner.adapter = arrayAdapter

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                chosenRecipe = spinnerItemList[pos]
                stepList = createStringList(chosenRecipe)
                if (viewModel.negativeAmount(chosenRecipe, stockList)) {
                    val dialog = DialogQuestionFragment(
                        this@BrewFragment::onDialogQuestionConfirm,
                        this@BrewFragment::onDialogQuestionAbort
                    )
                    dialog.isCancelable = false
                    dialog.show(childFragmentManager, "questionDialog")
                }
                adapter.submitList(stepList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.brewInfoButton.setOnClickListener {
            val dialog = DialogInstructionBrewFragment()
            dialog.isCancelable = false
            dialog.show(childFragmentManager, "brewInfoDialog")
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") && startTimer)
                Toast.makeText(context, R.string.timer_already_running, Toast.LENGTH_SHORT).show()
            else if (binding.brewTimerStartButton.text.equals("Start") && !startTimer) {
                startTimer = true
                timerStart(milliFromItem)
            } else if (binding.brewTimerStartButton.text.equals(getString(R.string.along))) {
                timerStart(milliLeft)
                binding.brewTimerStartButton.text = "Start"
                binding.brewTimerStopButton.text = "Stop"
                startTimer = true
            }
        }

        binding.brewTimerStopButton.setOnClickListener {
            countDownTimer?.let {
                if (binding.brewTimerStopButton.text.equals("Stop") && startTimer) {
                    it.cancel()
                    binding.brewTimerStartButton.text = getString(R.string.along)
                    binding.brewTimerStopButton.text = "Cancel"
                } else if (binding.brewTimerStopButton.text.equals("Cancel")) {
                    it.cancel()
                    binding.brewTimerText.text = getString(R.string.time_dummy)
                    binding.brewTimerStartButton.text = "Start"
                    binding.brewTimerStopButton.text = "Stop"
                    milliFromItem = 0
                    startTimer = false
                }
            }
        }

        binding.brewFinishButton.setOnClickListener {
            if (stepList.isNotEmpty()) {
                val dialog = DialogCookingFragment(
                    false, chosenRecipe.toBrewHistoryItem(), this::onDialogCookingConfirm
                )
                dialog.isCancelable = false
                dialog.show(childFragmentManager, "cookingDialog")
            } else
                Toast.makeText(context, R.string.make_recipe, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDialogCookingConfirm() {
        if (withSubtract)
            viewModel.updateDatabase(chosenRecipe, stockList)
        findNavController()
            .navigate(BrewFragmentDirections.actionBrewFragmentToBrewHistoryFragment())
    }

    fun onDialogQuestionConfirm(subtract: Boolean) {
        withSubtract = subtract
    }

    fun onDialogQuestionAbort() {
        findNavController().navigate(BrewFragmentDirections.actionBrewFragmentToHomeFragment())
    }


    @SuppressLint("SetTextI18n")
    private fun onItemClick(brewItem: StepItem) {
        if (startTimer)
            Toast.makeText(context, R.string.timer_already_running, Toast.LENGTH_SHORT).show()
        else {
            binding.brewTimerStartButton.text = "Start"
            if (brewItem.brewTime != "") {
                milliFromItem = brewItem.brewTime.toLong() * 60000
                timerStart(milliFromItem)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timerStart(timeInMilli: Long) {
        binding.brewTimerText.text = minutes(timeInMilli) + "00"
        if (startTimer) {
            countDownTimer = object : CountDownTimer((timeInMilli), 1000) {
                override fun onTick(untilFinish: Long) {
                    milliLeft = untilFinish
                    binding.brewTimerText.text = minutes(untilFinish) + seconds(untilFinish)
                }

                override fun onFinish() {
                    binding.brewTimerText.text = getString(R.string.time_dummy)
                    binding.brewTimerStartButton.text = "Start"
                    milliFromItem = 0
                    startTimer = false
                }
            }.start()
        }
    }

    fun minutes(millis: Long): String {
        if (millis / 60000 < 1) return "00:"
        if (millis / 60000 in 1..9) return "0" + (millis / 60000) + ":"
        return "" + (millis / 60000) + ":"
    }

    fun seconds(millis: Long): String {
        var millisSeconds: Long = millis
        while (millisSeconds >= 60000)
            millisSeconds -= 60000

        return when (millisSeconds / 1000) {
            in 0..0 -> "00"
            in 1..9 -> "0" + +(millisSeconds / 1000)
            else -> "" + millisSeconds / 1000
        }
    }

    private fun createStringList(item: RecipeItem): List<StepItem> {
        val newBrewList = mutableListOf<StepItem>()
        var counter = 1

        item.maltList.forEach {
            newBrewList.add(
                StepItem(
                    counter = counter,
                    itemString = it.stockName + " " + it.stockAmount,
                    brewTime = ""
                )
            )
            counter++
        }

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.grinding_malt),
                brewTime = ""
            )
        )
        counter++

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.first_brew) + ": " + item.mainBrew.firstBrew,
                brewTime = ""
            )
        )
        counter++

        item.restList.forEach {
            newBrewList.add(
                StepItem(
                    counter = counter,
                    itemString = it.restTemp + getString(R.string.unit_of_measurement_temp),
                    brewTime = it.restTime
                )
            )
            counter++
        }

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.second_brew) + ": " + item.mainBrew.secondBrew,
                brewTime = ""
            )
        )
        counter++

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.remove_malt),
                brewTime = ""
            )
        )
        counter++

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.heat_to_about_temperature),
                brewTime = ""
            )
        )
        counter++

        var hoppingListString = ""

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                hoppingListString += hop.stockName + " " + hop.stockAmount + " "
            }

            newBrewList.add(
                StepItem(
                    counter = counter,
                    itemString = hoppingListString,
                    brewTime = hopping.hoppingTime
                )
            )
            hoppingListString = ""
            counter++
        }

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.pipeing),
                brewTime = ""
            )
        )
        counter++

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = getString(R.string.let_it_cool_down),
                brewTime = ""
            )
        )
        counter++

        newBrewList.add(
            StepItem(
                counter = counter,
                itemString = item.yeast.stockName + " " + item.yeast.stockAmount,
                brewTime = ""
            )
        )
        counter++

        return newBrewList
    }
}