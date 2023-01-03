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
import com.example.brauport.model.stock.SNoAmount
import com.example.brauport.model.stock.StockItem

class RecipeStockAdapter(
    private val onItemAdd: (StockItem, String) -> Unit,
    private val onDeleteClick: (SNoAmount) -> Unit
) : ListAdapter<StockItem, RecipeStockAdapter.RecipeStockViewHolder>(DiffCallback) {

    lateinit var context: Context

    class RecipeStockViewHolder(val binding: CardRecipeStockBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeStockViewHolder {
        context = parent.context
        return RecipeStockViewHolder(
            CardRecipeStockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecipeStockViewHolder, position: Int) =
        with(holder.binding) {
            val item = getItem(position)

            rStockItemTitle.text = item.stockName
            rStockItemAmount.text = item.stockAmount + "g"

            rStockItemAdd.setOnClickListener {
                val newAmount = rStockItemAmountInput.text.toString()

                if (newAmount == "")
                    Toast.makeText(context, R.string.fill_amount, Toast.LENGTH_SHORT).show()
                else
                    onItemAdd(item, rStockItemAmountInput.text.toString())
            }

            rStockItemDelete.setOnClickListener { onDeleteClick(item.toSNoAmount()) }
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