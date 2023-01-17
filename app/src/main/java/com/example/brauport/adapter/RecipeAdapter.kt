package com.example.brauport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauport.databinding.CardRecipeBinding
import com.example.brauport.model.recipe.RecipeItem

class RecipeAdapter(
    private val onInspectClick: (RecipeItem) -> Unit,
    private val onItemClick: (RecipeItem) -> Unit,
    private val onDeleteClick: (RecipeItem) -> Unit
) : ListAdapter<RecipeItem, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    class RecipeViewHolder(val binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            CardRecipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        itemTitle.text = item.name

        inspectButton.setOnClickListener {
            onInspectClick(item)
        }

        root.setOnClickListener {
            onItemClick(item)
        }

        deleteButton.setOnClickListener {
            onDeleteClick(item)
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