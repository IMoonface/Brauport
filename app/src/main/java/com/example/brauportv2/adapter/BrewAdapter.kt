package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardBrewBinding
import com.example.brauportv2.model.BrewItem

class BrewAdapter(
    private val onItemClick: (BrewItem) -> Unit
): ListAdapter<BrewItem, BrewAdapter.RecipeViewHolder>(DiffCallback) {

    class RecipeViewHolder(val binding: CardBrewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(CardBrewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        brewItemTitle.text = item.itemString

        brewToggleButton.isChecked = item.state
        brewToggleButton.setOnCheckedChangeListener { _, checked ->
            item.state = checked
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

    object DiffCallback : DiffUtil.ItemCallback<BrewItem>() {
        override fun areItemsTheSame(oldItem: BrewItem, newItem: BrewItem)
            : Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: BrewItem, newItem: BrewItem)
            : Boolean = oldItem == newItem
    }
}