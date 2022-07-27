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
import com.example.brauportv2.adapter.BrewAdapter
import com.example.brauportv2.databinding.FragmentBrewBinding
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.mapper.toStockItem
import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.recipeModel.RecipeItem
import com.example.brauportv2.ui.`object`.RecipeDataSource.recipeItemList
import com.example.brauportv2.ui.`object`.StringCreator.createStringList
import com.example.brauportv2.ui.dialog.DialogCookingFragment
import com.example.brauportv2.ui.viewmodel.StockViewModel
import com.example.brauportv2.ui.viewmodel.StockViewModelFactory
import kotlinx.coroutines.launch

class BrewFragment : Fragment() {

    private var _binding: FragmentBrewBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: BrewAdapter
    private var spinnerOptions: MutableList<String> = mutableListOf()
    private var countDownTimer: CountDownTimer? = null
    private lateinit var chosenRecipe: RecipeItem
    private lateinit var stockStartList: List<StockItem>
    private var milliLeft: Long = 0
    private var milliFromItem: Long = 0
    private var startTimer = false
    private var possible = true

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrewBinding.inflate(inflater, container, false)

        adapter = BrewAdapter(this::onItemClick)
        binding.brewRecyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allStockItems.collect { it -> stockStartList = it.map { it.toStockItem() } }
        }

        recipeItemList.forEach {
            spinnerOptions.add(it.recipeName)
        }

        binding.brewSpinner.adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerOptions)

        binding.brewSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                binding.brewTimerText.text = "Bitte Item anklicken!"
                chosenRecipe = recipeItemList[pos]
                if (proveForNegAmount(chosenRecipe)) {
                    adapter.submitList(createStringList(chosenRecipe))
                } else {
                    //TODO Dialog öffnen und fragen ob man trotzdem ausführen möchte, obwohl der Bestand negativ wird
                }

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
                val dialog = DialogCookingFragment(chosenRecipe)
                dialog.show(childFragmentManager, "cookingDialog")

                if (dialog.abort)
                    Toast.makeText(
                        context,
                        "Rezept wurde nicht abgeschlossen",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    updateDatabase(chosenRecipe)

            } else
                Toast.makeText(context, "Es sind noch Schritte offen", Toast.LENGTH_SHORT)
                    .show()
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") &&
                binding.brewTimerText.text != "Bitte Item anklicken!" &&
                binding.brewTimerText.text != "Bitte Rezept erstellen!" && !startTimer
            ) {
                startTimer = true
                timerStart(milliFromItem)
            } else if (binding.brewTimerStartButton.text.equals("Weiter")) {
                timerStart(milliLeft)
                binding.brewTimerStartButton.text = "Start"
                binding.brewTimerStopButton.text = "Stop"
                startTimer = true
            } else if (binding.brewTimerStartButton.text.equals("Start") && startTimer) {
                Toast.makeText(context, "Der Timer läuft bereits!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.brewTimerStopButton.setOnClickListener {
            countDownTimer?.let {
                if (binding.brewTimerStopButton.text.equals("Stop") &&
                    binding.brewTimerText.text != "Bitte Item anklicken!"
                ) {
                    it.cancel()
                    binding.brewTimerStartButton.text = "Weiter"
                    binding.brewTimerStopButton.text = "Cancel"
                } else if (binding.brewTimerText.text == "Bitte Item anklicken!")
                    Toast.makeText(
                        context, "Es wurde noch keine Zeit ausgewählt!", Toast.LENGTH_SHORT
                    ).show()
                else {
                    it.cancel()
                    binding.brewTimerText.text = "Ende"
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
    private fun onItemClick(brewItem: BrewItem) {
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

    @SuppressLint("SetTextI18n")
    private fun timerStart(timeInMilli: Long) {
        binding.brewTimerText.text = minutes(timeInMilli) + ":00"
        if (startTimer) {
            countDownTimer = object : CountDownTimer((timeInMilli), 1000) {
                override fun onTick(untilFinish: Long) {
                    milliLeft = untilFinish
                    binding.brewTimerText.text = minutes(untilFinish) + ":" + seconds(untilFinish)
                }

                override fun onFinish() {
                    binding.brewTimerText.text = "Ende"
                    binding.brewTimerStartButton.text = "Start"
                    milliFromItem = 0
                    startTimer = false
                }
            }.start()
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

    private fun calcAmount(item: StockItem): String {
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val index = stockStartList.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val databaseAmount = stockStartList[index].stockAmount.substringBefore("g").toInt()
        return (databaseAmount - recipeAmount).toString() + "g"
    }

    private fun updateDatabase(recipeItem: RecipeItem) {
        recipeItem.maltList.forEach { malt ->
            viewModel.updateStock(malt.id, malt.itemType, malt.stockName, calcAmount(malt))
        }

        recipeItem.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                viewModel.updateStock(hop.id, hop.itemType, hop.stockName, calcAmount(hop))
            }
        }

        viewModel.updateStock(
            recipeItem.yeast.id,
            recipeItem.yeast.itemType,
            recipeItem.yeast.stockName,
            calcAmount(recipeItem.yeast)
        )
    }

    private fun calcForShortage(item: StockItem) {
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val index = stockStartList.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val databaseAmount = stockStartList[index].stockAmount.substringBefore("g").toInt()
        possible = databaseAmount - recipeAmount >= 0
    }

    private fun proveForNegAmount(recipeItem: RecipeItem) : Boolean {
        recipeItem.maltList.forEach { malt ->
            calcForShortage(malt)
        }

        recipeItem.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                calcForShortage(hop)
            }
        }

        calcForShortage(recipeItem.yeast)

        return possible
    }
}