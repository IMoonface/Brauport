package com.example.brauportv2.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
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

        adapter = BrewAdapter(this::onToggle, this::onItemClick)
        binding.brewRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStepLists.collect { list ->
                startList = list.map { it.toStepList() }.filter { it.rId == item.rId }
                adapter.submitList(createStringList(item))
            }
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") && !startTimer) {
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
                    binding.brewTimerText.text != getString(R.string.time_dummy)
                ) {
                    it.cancel()
                    binding.brewTimerStartButton.text = getString(R.string.along)
                    binding.brewTimerStopButton.text = "Cancel"
                } else {
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
    private fun onItemClick(brewItem: StepItem) {
        if (startTimer)
            Toast.makeText(context, "Es läuft schon ein Timer!", Toast.LENGTH_SHORT).show()
        else {
            binding.brewTimerStartButton.text = "Start"
            if (brewItem.brewTime != "") {
                milliFromItem = brewItem.brewTime.toLong() * 60000
                timerStart(milliFromItem)
            }
        }
    }

    private fun onToggle(steps: List<StepItem>) {
        if (startList.isNotEmpty()) {
            viewModel.updateStepList(startList[0].sId, item.rId, steps)
            stepList = startList[0].steps
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
                    binding.brewTimerText.text = getString(R.string.end)
                    binding.brewTimerStartButton.text = "Start"
                    milliFromItem = 0
                    startTimer = false
                }
            }.start()
        }
    }

    private fun createStringList(item: RecipeItem): List<StepItem> {
        if (startList.isEmpty()) {
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
                newBrewList.add(StepItem(it.restTemp, it.restTime, false))
            }

            newBrewList.add(
                StepItem(
                    getString(R.string.second_brew) + ": " + item.mainBrew.secondBrew,
                    "",
                    false
                )
            )

            newBrewList.add(
                StepItem(
                    getString(R.string.remove_malt),
                    "",
                    false
                )
            )
            newBrewList.add(
                StepItem(
                    getString(R.string.heat_to_about_temperature),
                    "",
                    false
                )
            )

            var hoppingListString = ""
            item.hoppingList.forEach { hopping ->
                hopping.hopsList.forEach { hop ->
                    hoppingListString += hop.stockName + " " + hop.stockAmount + " "
                }
                newBrewList.add(StepItem(hoppingListString, hopping.hoppingTime, false))
                hoppingListString = ""
            }

            newBrewList.add(StepItem(getString(R.string.pipeing), "", false))

            newBrewList.add(
                StepItem(
                    getString(R.string.let_it_cool_down),
                    "",
                    false
                )
            )

            newBrewList.add(
                StepItem(
                    item.yeast.stockName + " " + item.yeast.stockAmount,
                    "",
                    false
                )
            )

            viewModel.addStepList(StepList(UUID.randomUUID().hashCode(), item.rId, newBrewList))

            return newBrewList
        }
        return startList[0].steps
    }
}