package com.example.brauportv2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardBrewHistoryBinding
import com.example.brauportv2.model.recipeModel.RecipeItem

class BrewHistoryAdapter
    : ListAdapter<RecipeItem, BrewHistoryAdapter.BrewHistoryViewHolder>(DiffCallback) {

    class BrewHistoryViewHolder(val binding: CardBrewHistoryBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrewHistoryViewHolder {
        return BrewHistoryViewHolder(
            CardBrewHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BrewHistoryViewHolder, position: Int) = with(holder.binding) {
        val item = getItem(position)
        brewHistoryTitle.text = item.recipeName
        brewHistoryEndDate.text = "Ende der Gärung: " + item.endOfFermentation

        brewHistoryInspect.setOnClickListener {
            //Todo: Dialog öffnen und Details anzeigen
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