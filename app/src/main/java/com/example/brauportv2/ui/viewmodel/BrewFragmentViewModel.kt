package com.example.brauportv2.ui.viewmodel

import com.example.brauportv2.model.BrewItem
import com.example.brauportv2.model.recipeModel.RecipeItem

class BrewFragmentViewModel {

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
}