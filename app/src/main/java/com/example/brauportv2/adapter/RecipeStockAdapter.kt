package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardRecipeStockBinding
import com.example.brauportv2.mapper.toRStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.recipeModel.RStockItem

class RecipeStockAdapter(
    private val onItemClick: (RStockItem) -> Unit,
    private val onDeleteClick: (RStockItem) -> Unit
) : ListAdapter<StockItem, RecipeStockAdapter.RecipeViewHolder>(DiffCallback) {

    class RecipeViewHolder(val binding: CardRecipeStockBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
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

        rStockAddButton.setOnClickListener {
            onItemClick(item.toRStockItem())
        }

        rStockDeleteButton.setOnClickListener {
            onDeleteClick(item.toRStockItem())
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