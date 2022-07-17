package com.example.brauportv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardHoppingBinding
import com.example.brauportv2.mapper.toRStockItem
import com.example.brauportv2.model.StockItem
import com.example.brauportv2.model.recipeModel.RStockItem

class RecipeHoppingAdapter(
    private val onItemAdd: (RStockItem, String) -> Unit,
    private val onItemDelete: (RStockItem) -> Unit,
) : ListAdapter<StockItem, RecipeHoppingAdapter.RecipeViewHolder>(DiffCallback) {

    lateinit var context: Context

    class RecipeViewHolder(val binding: CardHoppingBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        context = parent.context
        return RecipeViewHolder(CardHoppingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        rHoppingTitle.text = item.stockName
        rHoppingAmount.text = item.stockAmount

        rHoppingAdd.setOnClickListener {
            val newTime = rHoppingTimeInput.text.toString()

            if (newTime == "")
                Toast.makeText(context, "Bitte Zeit angeben!", Toast.LENGTH_SHORT).show()
            else
                onItemAdd(item.toRStockItem(), newTime)
        }

        rHoppingDelete.setOnClickListener {
            onItemDelete(item.toRStockItem())
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
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem):
                Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem):
                Boolean = oldItem == newItem
    }
}