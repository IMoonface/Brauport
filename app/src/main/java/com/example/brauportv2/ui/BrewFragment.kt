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
import com.example.brauportv2.model.recipeModel.RecipeDataSource.recipeItemList
import com.example.brauportv2.model.recipeModel.RecipeItem
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
    private lateinit var choosenRecipe: RecipeItem
    private lateinit var stockStartList: List<StockItem>
    private var milliLeft: Long = 0
    private var milliFromItem: Long = 0
    private var startTimer = false

    private val viewModel: StockViewModel by activityViewModels {
        StockViewModelFactory((activity?.application as BaseApplication).stockDatabase.stockDao())
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.brewTimerText.text = "Bitte Item anklicken!"
                choosenRecipe = recipeItemList[position]
                adapter.submitList(createStringList(choosenRecipe))
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
                val dialog = DialogCookingFragment(choosenRecipe)
                dialog.show(childFragmentManager, "brewHistoryDialog")

                if (dialog.abort)
                    Toast.makeText(
                        context,
                        "Rezept wurde nicht abgeschlossen",
                        Toast.LENGTH_SHORT
                    ).show()
                else
                    updateDatabase(choosenRecipe)

            } else
                Toast.makeText(context, "Es sind noch Schritte offen", Toast.LENGTH_SHORT)
                    .show()
        }

        binding.brewTimerStartButton.setOnClickListener {
            if (binding.brewTimerStartButton.text.equals("Start") &&
                binding.brewTimerText.text != "Bitte Item anklicken!" &&
                binding.brewTimerText.text != "Bitte Rezept erstellen!"
            ) {
                startTimer = true
                timerStart(milliFromItem)
            } else if (binding.brewTimerText.text != "Bitte Item anklicken!" &&
                binding.brewTimerText.text != "Bitte Rezept erstellen!"
            ) {
                timerStart(milliLeft)
                binding.brewTimerStopButton.text = "Stop"
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
                        context,
                        "Es wurde noch keine Zeit ausgewählt!",
                        Toast.LENGTH_SHORT
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
        if (startTimer) {
            Toast.makeText(context, "Es läuft schon ein Timer!", Toast.LENGTH_SHORT).show()
        } else {
            binding.brewTimerStartButton.text = "Start"
            if (brewItem.brewTime != "") {
                milliFromItem = brewItem.brewTime.toLong() * 60000
                timerStart(milliFromItem)
            }
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
                    binding.brewTimerStartButton.text = "Start"
                    milliFromItem = 0
                    startTimer = false
                }
            }.start()
        }
    }

    private fun createStringList(recipeItem: RecipeItem): List<BrewItem> {
        val newBrewList = emptyList<BrewItem>().toMutableList()
        recipeItem.maltList.forEach {
            newBrewList.add(
                BrewItem(
                    it.stockName + " " + it.stockAmount,
                    "",
                    false
                )
            )
        }

        newBrewList.add(BrewItem("Malz Schroten", "", false))

        newBrewList.add(
            BrewItem(
                "Hauptguss: " + recipeItem.mainBrew.firstBrew,
                "",
                false
            )
        )

        recipeItem.restList.forEach {
            newBrewList.add(
                BrewItem(
                    it.restTemp,
                    it.restTime.substringBefore("min"),
                    false
                )
            )
        }

        newBrewList.add(
            BrewItem(
                "Nachguss: " + recipeItem.mainBrew.secondBrew,
                "",
                false
            )
        )

        newBrewList.add(BrewItem("Malz entnehmen", "", false))
        newBrewList.add(BrewItem("Auf etwa Temperatur erhitzen", "", false))

        var hoppingListString = ""
        recipeItem.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                hoppingListString += hop.stockName + " " + hop.stockAmount + " "
            }
            newBrewList.add(BrewItem(hoppingListString, hopping.hoppingTime, false))
        }

        newBrewList.add(BrewItem("Schlauchen", "", false))
        newBrewList.add(BrewItem("Abkühlen lassen", "", false))
        newBrewList.add(
            BrewItem(
                recipeItem.yeast.stockName + " " + recipeItem.yeast.stockAmount,
                "",
                false
            )
        )

        return newBrewList
    }

    private fun calculateAmount(item: StockItem): String {
        val recipeAmount = item.stockAmount.substringBefore("g").toInt()
        val index = stockStartList.map { it.toSNoAmount() }.indexOf(item.toSNoAmount())
        val databaseAmount = stockStartList[index].stockAmount.substringBefore("g").toInt()
        return (databaseAmount - recipeAmount).toString() + "g"
    }

    private fun updateDatabase(recipeItem: RecipeItem) {
        recipeItem.maltList.forEach { stockItem ->
            viewModel.updateStock(
                stockItem.id,
                stockItem.itemType,
                stockItem.stockName,
                calculateAmount(stockItem)
            )
        }

        recipeItem.hoppingList.forEach { hopping ->
            hopping.hopsList.forEach { hop ->
                viewModel.updateStock(
                    hop.id,
                    hop.itemType,
                    hop.stockName,
                    calculateAmount(hop)
                )
            }
        }

        viewModel.updateStock(
            recipeItem.yeast.id,
            recipeItem.yeast.itemType,
            recipeItem.yeast.stockName,
            calculateAmount(recipeItem.yeast)
        )
    }
}