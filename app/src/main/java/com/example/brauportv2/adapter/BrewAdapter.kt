package com.example.brauportv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.databinding.CardBrewBinding
import com.example.brauportv2.model.brew.StepItem

class BrewAdapter(
    private val onItemClick: (StepItem) -> Unit
) : ListAdapter<StepItem, BrewAdapter.BrewViewHolder>(DiffCallback) {

    class BrewViewHolder(val binding: CardBrewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrewViewHolder {
        return BrewViewHolder(
            CardBrewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BrewViewHolder, position: Int) = with(holder.binding) {
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

    object DiffCallback : DiffUtil.ItemCallback<StepItem>() {
        override fun areItemsTheSame(oldItem: StepItem, newItem: StepItem): Boolean {
            return false
        }


        override fun areContentsTheSame(oldItem: StepItem, newItem: StepItem): Boolean {
            return false
        }
    }
}