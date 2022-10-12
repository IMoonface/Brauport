package com.example.brauportv2.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.brauportv2.R
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewDetailsBinding
import com.example.brauportv2.model.brew.StepItem
import com.example.brauportv2.model.recipe.RecipeItem

class BrewDetailsFragment(private val item: RecipeItem) : Fragment() {

    private var _binding: FragmentBrewDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var countDownTimer: CountDownTimer? = null
    private var milliFromItem: Long = 0
    private var milliLeft: Long = 0
    private var startTimer = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrewDetailsBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)
        binding.brewRecyclerView.adapter = adapter
        stepList = createStringList(item)
        adapter.submitList(stepList)

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

    override fun onDestroyView() {
        super.onDestroyView()
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

    companion object {
        lateinit var stepList: List<StepItem>
    }
}