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
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItemList
import com.example.brauportv2.model.recipeModel.RecipeItem

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private lateinit var countDownTimer: CountDownTimer
    private var milliLeft: Long = 0
    private var milliFromItem: Long = 0
    private var startTimer = false

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)
        binding.brewRecyclerView.adapter = adapter

        recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                adapter.submitList(createStringList(recipeItemList[position]))
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.brewFinishButton.setOnClickListener {
            var finished = false
            adapter.currentList.forEach {
                finished = it.state
            }

            if (finished) {
                Toast.makeText(context, "Rezept abgeschlossen", Toast.LENGTH_SHORT)
                    .show()
            } else
                Toast.makeText(context, "Es sind noch Schritte offen", Toast.LENGTH_SHORT)
                    .show()
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") &&
                binding.brewTimerText.text != "timer") {
                startTimer = true
                timerStart(milliFromItem)
            } else if (binding.brewTimerText.text != "timer") {
                startTimer = true
                timerStart(milliLeft)
            }
        }

        binding.brewTimerStopButton.setOnClickListener {
            countDownTimer.cancel()
            binding.brewTimerStartButton.text = "Weiter"
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun onItemClick(brewItem: BrewItem) {
        binding.brewTimerStartButton.text = "Start"
        if (brewItem.brewTime != "") {
            milliFromItem = brewItem.brewTime.toLong() * 60000
            timerStart(milliFromItem)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun timerStart(timeInMilli: Long) {
        binding.brewTimerText.text = (timeInMilli / (1000 * 60)).toString() + ":00"
        if (startTimer) {
            countDownTimer = object : CountDownTimer((timeInMilli), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    milliLeft = millisUntilFinished
                    val min = (millisUntilFinished / (1000 * 60))
                    val sec = ((millisUntilFinished / 1000) - min * 60)
                    binding.brewTimerText.text = "$min:$sec"
                }

                override fun onFinish() {
                    binding.brewTimerText.text = "Ende"
                }
            }.start()
            startTimer = false
        }
    }

    private fun minutes(millis: Long): String {
        if (millis / 60000 < 1) return "00"
        if (millis / 60000 in 1..9) return "0" + (millis / 60000)
        return "" + (millis / 60000)
    }

    private fun seconds(millis: Long): String {
        var millisSeconds: Long = millis
        while (millisSeconds >= 60000) {
            millisSeconds -= 60000
        }
        return when (millisSeconds / 1000) {
            in 0..0 -> "00"
            in 1..9 -> "0" + +(millisSeconds / 1000)
            else -> "" + millisSeconds / 1000
        }
    }

    fun createStringList(recipeItem: RecipeItem): List<BrewItem> {
        val newBrewList = emptyList<BrewItem>().toMutableList()
        recipeItem.maltList.forEach {
            newBrewList.add(BrewItem(it.rStockName + " " + it.rStockAmount, "", false))
        }

        newBrewList.add(BrewItem("Malz Schroten", "333", false))
        newBrewList.add(BrewItem("Hauptguss: " + recipeItem.mainBrew.firstBrew, "", false))

        //Rasten Frage: Alle auf einmal???

        newBrewList.add(BrewItem("Nachguss: " + recipeItem.mainBrew.secondBrew, "", false))
        newBrewList.add(BrewItem("Malz entnehmen", "", false))
        newBrewList.add(BrewItem("Auf etwa Temperatur erhitzen", "", false))
        recipeItem.hoppingList.forEach {
            newBrewList.add(
                BrewItem(
                    it.hoppingName + " " + it.hoppingAmount,
                    it.hoppingTime,
                    false
                )
            )
        }

        newBrewList.add(BrewItem("Schlauchen", "", false))
        newBrewList.add(BrewItem("Abkühlen lassen", "", false))
        newBrewList.add(
            BrewItem(
                recipeItem.yeast.rStockName + " " + recipeItem.yeast.rStockAmount,
                "",
                false
            )
        )

        newBrewList.add(BrewItem("Abfüllen evt. Flaschengärung", "", false))
        newBrewList.add(BrewItem("Genießen", "", false))

        return newBrewList
    }
}