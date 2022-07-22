package com.example.brauportv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardRecipeStockBinding
import com.example.brauportv2.mapper.toRStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.recipeModel.Hopping
import com.example.brauportv2.model.recipeModel.RStockItem

class RecipeHoppingAdapter
    : ListAdapter<StockItem, RecipeHoppingAdapter.RecipeViewHolder>(DiffCallback) {

    lateinit var context: Context
    var newhopsList : Hopping = Hopping(emptyList<RStockItem>().toMutableList(), "")

    class RecipeViewHolder(val binding: CardRecipeStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        context = parent.context
        return RecipeViewHolder(CardRecipeStockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        rStockItemTitle.text = item.stockName
        rStockItemAmount.text = item.stockAmount

        rStockItemAdd.setOnClickListener {
            val newAmount = rStockItemAmountInput.text.toString()

            if (newAmount == "")
                Toast.makeText(context, "Bitte Menge angeben!", Toast.LENGTH_SHORT).show()
            else if (newAmount.substringBefore("g").toInt() >
                item.stockAmount.substringBefore("g").toInt()
            )
                Toast.makeText(context, "Menge im Bestand nicht vorhanden!", Toast.LENGTH_SHORT)
                    .show()
            else {
                val newItem = item.toRStockItem()
                newItem.rStockAmount = newAmount
                newhopsList.hopsList.add(newItem)
                Toast.makeText(context, "Hopfen wurde hinzugefügt!", Toast.LENGTH_SHORT).show()
            }
        }

        rStockItemDelete.setOnClickListener {
            if (newhopsList.hopsList == emptyList<RStockItem>().toMutableList()) {
                Toast.makeText(context, "Kein Hopfen vorhanden!", Toast.LENGTH_SHORT).show()
            } else {
                //hier nochmal gucken
                newhopsList.hopsList.remove(item.toRStockItem())
                Toast.makeText(context, "Hopfen wurde entfernt!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem)
            : Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem)
            : Boolean = oldItem == newItem
    }
}