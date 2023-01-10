package com.example.brauport.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauport.R
import com.example.brauport.databinding.CardBrewHistoryBinding
import com.example.brauport.model.recipe.RecipeItem

class BrewHistoryAdapter(
    private val onInspectClick: (RecipeItem) -> Unit,
    private val onItemClick: (RecipeItem) -> Unit,
    private val onDeleteClick: (RecipeItem) -> Unit
) : ListAdapter<RecipeItem, BrewHistoryAdapter.BrewHistoryViewHolder>(DiffCallback) {

    lateinit var context: Context

    class BrewHistoryViewHolder(val binding: CardBrewHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrewHistoryViewHolder {
        context = parent.context
        return BrewHistoryViewHolder(
            CardBrewHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BrewHistoryViewHolder, position: Int) =
        with(holder.binding) {
            val item = getItem(position)

            itemTitle.text = item.name
            itemEndDate.text = context
                .getString(R.string.inspect_end_of_fermentation) + " " + item.endOfFermentation

            if (item.cardColor == Color.GRAY) {

                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(context, R.color.finished_brew_history_item)
                )

                deleteButton.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.finished_brew_history_item_button)
                )

                inspectButton.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.finished_brew_history_item_button)
                )
            }

            inspectButton.setOnClickListener {
                onInspectClick(item)
            }

            deleteButton.setOnClickListener {
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

    object DiffCallback : DiffUtil.ItemCallback<RecipeItem>() {
        override fun areItemsTheSame(oldItem: RecipeItem, newItem: RecipeItem)
                : Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem)
                : Boolean = oldItem == newItem
    }
}