package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.R
import com.example.brauportv2.databinding.CardBrewBinding

class BrewAdapter : ListAdapter<String, BrewAdapter.RecipeViewHolder>(DiffCallback) {

    private var toogle = false

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
        brewItemTitle.text = item

        brewCheckButton.setOnClickListener {
            if (toogle) {
                toogle = false
                brewCheckButton.setIconResource(R.drawable.ic_close)
            } else {
                toogle = true
                brewCheckButton.setIconResource(R.drawable.ic_check)
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

    object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String)
            : Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String)
            : Boolean = oldItem == newItem
    }
}