package com.jamieadkins.gwent.filter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView

import com.jamieadkins.gwent.R
import com.jamieadkins.gwent.bus.*
import com.jamieadkins.gwent.core.GwentStringHelper
import kotterknife.bindView

class FilterRecyclerViewAdapter : RecyclerView.Adapter<FilterRecyclerViewAdapter.FilterViewHolder>() {
    var filters = listOf<FilterableItem>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checkable, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val filter = filters[position]

        holder.checkBox.isChecked = filter.isChecked

        holder.itemView.setOnClickListener {
            val newState = !holder.checkBox.isChecked
            holder.checkBox.isChecked = newState
            when (filter) {
                is FactionFilter -> RxBus.post(FilterChangeEvent(FactionFilter(filter.faction, newState)))
                is ColourFilter -> RxBus.post(FilterChangeEvent(ColourFilter(filter.colour, newState)))
                is RarityFilter -> RxBus.post(FilterChangeEvent(RarityFilter(filter.rarity, newState)))
            }
        }

        when (filter) {
            is FactionFilter -> holder.tvName.text = GwentStringHelper.getFactionString(holder.itemView.context, filter.faction)
            is ColourFilter -> holder.tvName.text = GwentStringHelper.getColourString(holder.itemView.context, filter.colour)
            is RarityFilter -> holder.tvName.text = GwentStringHelper.getRarityString(holder.itemView.context, filter.rarity)
        }
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    inner class FilterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName by bindView<TextView>(R.id.name)
        val checkBox by bindView<CheckBox>(R.id.checkbox)
    }
}
