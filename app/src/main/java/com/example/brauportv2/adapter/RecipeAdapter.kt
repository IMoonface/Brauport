package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardRecipeBinding
import com.example.brauportv2.model.recipeModel.RecipeItem

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
        recipeItemTitle.text = item.recipeName

        recipeInspectButton.setOnClickListener {
            onInspectClick(item)
        }

        root.setOnClickListener {
            onItemClick(item)
        }

        recipeDeleteButton.setOnClickListener {
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
                : Boolean = oldItem.rId == newItem.rId

        override fun areContentsTheSame(oldItem: RecipeItem, newItem: RecipeItem)
                : Boolean = oldItem == newItem
    }
}