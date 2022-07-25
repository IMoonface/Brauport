package com.example.brauportv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardRecipeStockBinding
import com.example.brauportv2.mapper.toSNoAmount
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.recipeModel.Hopping

class HoppingAdapter : ListAdapter<StockItem, HoppingAdapter.HoppingViewHolder>(DiffCallback) {

    lateinit var context: Context
    var newhopsList: Hopping = Hopping(emptyList<StockItem>().toMutableList(), "")

    class HoppingViewHolder(val binding: CardRecipeStockBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoppingViewHolder {
        context = parent.context
        return HoppingViewHolder(
            CardRecipeStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HoppingViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        rStockItemTitle.text = item.stockName
        rStockItemAmount.text = item.stockAmount

        rStockItemAdd.setOnClickListener {
            val newAmount = rStockItemAmountInput.text.toString()

            if (newAmount == "")
                Toast.makeText(context, "Bitte Menge angeben!", Toast.LENGTH_SHORT).show()
            else if (newhopsList.hopsList.map { it.toSNoAmount() }.contains(item.toSNoAmount())) {
                Toast.makeText(context, "Hopfen ist schon vorhanden!", Toast.LENGTH_SHORT).show()
            } else {
                item.stockAmount = newAmount
                newhopsList.hopsList.add(item)
                Toast.makeText(context, "Hopfen wurde hinzugefügt!", Toast.LENGTH_SHORT).show()
            }
        }

        rStockItemDelete.setOnClickListener {
            if (newhopsList.hopsList == emptyList<StockItem>().toMutableList()) {
                Toast.makeText(context, "Kein Hopfen vorhanden!", Toast.LENGTH_SHORT).show()
            } else {
                val index = newhopsList.hopsList.map {
                    it.toSNoAmount()
                }.indexOf(item.toSNoAmount())
                newhopsList.hopsList.removeAt(index)
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