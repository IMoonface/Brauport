package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardStockBinding
import com.example.brauportv2.model.StockItem

class StockAdapter(
    private val onItemClick: (StockItem) -> Unit,
    private val onDeleteClick: (StockItem) -> Unit
): ListAdapter<StockItem, StockAdapter.MaltViewHolder>(DiffCallback) {

    class MaltViewHolder(val binding: CardStockBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaltViewHolder {
        return MaltViewHolder(CardStockBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: MaltViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        stockItemTitle.text = item.stockName
        stockItemAmount.text = item.stockAmount

        stockDeleteButton.setOnClickListener {
            onDeleteClick(item)
        }

        root.setOnClickListener {
            onItemClick(item)
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