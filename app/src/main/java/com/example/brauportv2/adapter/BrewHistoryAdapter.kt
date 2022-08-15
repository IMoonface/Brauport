package com.example.brauportv2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brauportv2.R
import com.example.brauportv2.databinding.CardBrewHistoryBinding
import com.example.brauportv2.model.brewHistory.BrewHistoryItem

class BrewHistoryAdapter(
    private val onInspectClick: (BrewHistoryItem) -> Unit,
    private val onItemClick: (BrewHistoryItem) -> Unit
) : ListAdapter<BrewHistoryItem, BrewHistoryAdapter.BrewHistoryViewHolder>(DiffCallback) {

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
            brewHistoryTitle.text = item.bName
            brewHistoryEndDate.text =
                context.getString(R.string.inspect_end_of_fermentation) + item.bEndOfFermentation

            brewHistoryInspect.setOnClickListener {
                onInspectClick(item)
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

    object DiffCallback : DiffUtil.ItemCallback<BrewHistoryItem>() {
        override fun areItemsTheSame(oldItem: BrewHistoryItem, newItem: BrewHistoryItem)
                : Boolean = oldItem.bId == newItem.bId

        override fun areContentsTheSame(oldItem: BrewHistoryItem, newItem: BrewHistoryItem)
                : Boolean = oldItem == newItem
    }
}