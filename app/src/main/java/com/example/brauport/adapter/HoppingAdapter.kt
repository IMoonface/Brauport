package com.example.brauport.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauport.R
import com.example.brauport.databinding.CardRecipeStockBinding
import com.example.brauport.mapper.toSNoAmount
import com.example.brauport.model.stock.StockItem

class HoppingAdapter : ListAdapter<StockItem, HoppingAdapter.HoppingViewHolder>(DiffCallback) {

    lateinit var context: Context
    var hopList = mutableListOf<StockItem>()

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HoppingViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)

        itemTitle.text = item.stockName
        itemAmount.text = item.stockAmount + "g"

        addButton.setOnClickListener {
            val itemAmount = rStockItemAmountInput.text.toString()

            if (itemAmount == "")
                Toast.makeText(context, R.string.fill_amount, Toast.LENGTH_SHORT).show()
            else if (hopList.map { it.toSNoAmount() }.contains(item.toSNoAmount())) {
                Toast.makeText(context, R.string.hop_exists, Toast.LENGTH_SHORT).show()
            } else {
                item.stockAmount = itemAmount
                hopList.add(item)
                Toast.makeText(context, R.string.added_hop, Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            if (hopList.map { it.toSNoAmount() }.indexOf(item.toSNoAmount()) == -1) {
                Toast.makeText(context, R.string.hop_not_found, Toast.LENGTH_SHORT).show()
            } else {
                hopList.removeAt(hopList.map { it.toSNoAmount() }.indexOf(item.toSNoAmount()))
                Toast.makeText(context, R.string.deleted_hop, Toast.LENGTH_SHORT).show()
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