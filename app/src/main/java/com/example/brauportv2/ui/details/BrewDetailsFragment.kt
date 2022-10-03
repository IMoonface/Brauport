package com.example.brauportv2.ui.details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.brauportv2.BaseApplication
import com.example.brauportv2.R
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewDetailsBinding
import com.example.brauportv2.mapper.toStepList
import com.example.brauportv2.model.brew.StepItem
import com.example.brauportv2.model.brew.StepList
import com.example.brauportv2.model.recipe.RecipeItem
import com.example.brauportv2.ui.objects.RecipeDataSource.stepList
import com.example.brauportv2.ui.viewModel.BrewDetailsViewModel
import com.example.brauportv2.ui.viewModel.BrewDetailsViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class BrewDetailsFragment(private val item: RecipeItem) : Fragment() {

    private var _binding: FragmentBrewDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var countDownTimer: CountDownTimer? = null
    private var milliFromItem: Long = 0
    private var milliLeft: Long = 0
    private var startTimer = false
    private var startList: List<StepList> = emptyList()

    private val viewModel: BrewDetailsViewModel by activityViewModels {
        BrewDetailsViewModelFactory(
            (activity?.application as BaseApplication).stepDatabase.stepDao()
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewDetailsBinding.inflate(inflater, container, false)

        val sharedPref = activity?.getSharedPreferences("myPref", Context.MODE_PRIVATE)

        adapter = BrewAdapter(this::onItemClick, this::onToggle)
        binding.brewRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStepLists.collect { list ->
                startList = list.map { it.toStepList() }.filter { it.rId == item.rId }

                sharedPref?.let {
                    val lastRecipeId = sharedPref.getInt("lastRecipeId", 0)
                    val lastUpdatedRId = sharedPref.getInt("lastUpdatedRId", 0)

                    //Hier nochmal nachschauen
                    if (startList.size >= 2) {
                        viewModel.deleteStepList(startList[1])
                    }

                    if (lastRecipeId == item.rId && startList.isNotEmpty()) {
                        viewModel.deleteStepList(startList[0])

                        sharedPref.edit().apply {
                            putInt("lastRecipeId", 0)
                            apply()
                        }

                        startList = emptyList()
                    }

                    if (lastUpdatedRId == item.rId && startList.isNotEmpty()) {
                        Log.i("bin drin", startList[0].toString())
                        viewModel.deleteStepList(startList[0])

                        sharedPref.edit().apply {
                            putInt("lastUpdatedRId", 0)
                            apply()
                        }

                        startList = emptyList()
                    }

                    stepList = if (startList.isEmpty()) {
                        adapter.submitList(createStringList(item))
                        adapter.currentList
                    } else {
                        adapter.submitList(startList[0].steps)
                        adapter.currentList
                    }
                }
            }
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

        return binding.root
    }

    private fun onToggle(steps: List<StepItem>) {
        stepList = steps
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.updateStepList(startList[0].sId, item.rId, adapter.currentList)
        _binding = null
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
        binding.brewTimerText.text = viewModel.minutes(timeInMilli) + ":00"
        if (startTimer) {
            countDownTimer = object : CountDownTimer((timeInMilli), 1000) {
                override fun onTick(untilFinish: Long) {
                    milliLeft = untilFinish
                    binding.brewTimerText.text =
                        viewModel.minutes(untilFinish) + ":" + viewModel.seconds(untilFinish)
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

    private fun createStringList(item: RecipeItem): List<StepItem> {
        val newBrewList = mutableListOf<StepItem>()

        item.maltList.forEach {
            newBrewList.add(
                StepItem(it.stockName + " " + it.stockAmount, "", false)
            )
        }

        newBrewList.add(StepItem(getString(R.string.grinding_malt), "", false))

        newBrewList.add(
            StepItem(
                getString(R.string.first_brew) + ": " + item.mainBrew.firstBrew,
                "",
                false
            )
        )

        item.restList.forEach {
            newBrewList.add(
                StepItem(
                    it.restTemp + getString(R.string.unit_of_measurement_temp),
                    it.restTime,
                    false
                )
            )
        }

        newBrewList.add(
            StepItem(
                getString(R.string.second_brew) + ": " + item.mainBrew.secondBrew,
                "",
                false
            )
        )

        newBrewList.add(StepItem(getString(R.string.remove_malt), "", false))

        newBrewList.add(
            StepItem(getString(R.string.heat_to_about_temperature), "", false)
        )

        var hoppingListString = ""

        item.hoppingList.forEach { hopping ->
            hopping.hopList.forEach { hop ->
                hoppingListString += hop.stockName + " " + hop.stockAmount + " "
            }

            newBrewList.add(StepItem(hoppingListString, hopping.hoppingTime, false))
            hoppingListString = ""
        }

        newBrewList.add(StepItem(getString(R.string.pipeing), "", false))

        newBrewList.add(StepItem(getString(R.string.let_it_cool_down), "", false))

        newBrewList.add(
            StepItem(
                item.yeast.stockName + " " + item.yeast.stockAmount,
                "",
                false
            )
        )

        viewModel.addStepList(
            StepList(UUID.randomUUID().hashCode(), item.rId, newBrewList)
        )

        return newBrewList
    }
}