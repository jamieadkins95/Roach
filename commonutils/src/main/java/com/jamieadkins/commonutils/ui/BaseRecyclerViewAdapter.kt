package com.jamieadkins.commonutils.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.jamieadkins.commonutils.R
import com.jamieadkins.commonutils.mvp2.applySchedulers
import io.reactivex.Completable
import io.reactivex.Single

import java.util.ArrayList

/**
 * Holds base logic for recycler view adapters.
 */

abstract class BaseRecyclerViewAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var items: List<RecyclerViewItem> = mutableListOf()
        set(value) {
            calculateDiffAsync(DiffUtilCallback(items, value))
                    .applySchedulers()
                    .subscribe { diffResult: DiffUtil.DiffResult ->
                        field = value
                        diffResult.dispatchUpdatesTo(this)
                    }
        }

    override fun getItemCount(): Int {
        return items.size
    }

    fun calculateDiffAsync(callback: DiffUtil.Callback) : Single<DiffUtil.DiffResult> {
        return Single.just(DiffUtil.calculateDiff(callback))
    }

    fun addItem(item: RecyclerViewItem) {
        val newItems = items.toMutableList()
        val index = newItems.indexOf(item)
        if (index == -1) {
            newItems.add(item)
        } else {
            newItems[index] = item
        }
        items = newItems
    }

    fun removeItem(item: RecyclerViewItem) {
        val newItems = items.toMutableList()
        newItems.remove(item)
        items = newItems
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            Header.TYPE_HEADER -> return HeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false))
            SubHeader.TYPE_SUB_HEADER -> return SubHeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_subheader, parent, false))
            GoogleNowSubHeader.TYPE_GOOGLE_NOW_SUB_HEADER -> return GoogleNowSubHeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_google_subheader, parent, false))
            else -> throw RuntimeException("Detail level has not been implemented.")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindItem(items[position])
    }
}
