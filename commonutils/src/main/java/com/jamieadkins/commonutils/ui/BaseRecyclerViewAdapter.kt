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
        addItem(-1, item)
    }

    fun addItem(position: Int, item: RecyclerViewItem) {
        if (items !is MutableList<RecyclerViewItem>) {
            return
        }

        if (item in items) {
            val index = items.indexOf(item)
            if (!item.areContentsTheSame(items[index])) {
                (items as MutableList<RecyclerViewItem>)[index] = item
                notifyItemChanged(index)
            }
        } else {
            if (position != -1) {
                (items as MutableList<RecyclerViewItem>).add(position, item)
                notifyItemInserted(position)
            } else {
                (items as MutableList<RecyclerViewItem>).add(item)
                notifyItemInserted(itemCount - 1)
            }
        }
    }

    fun removeItem(item: RecyclerViewItem) {
        if (items !is MutableList<RecyclerViewItem>) {
            return
        }

        if (item in items) {
            val index = items.indexOf(item)
            (items as MutableList<RecyclerViewItem>).removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun removeItemAt(position: Int) {
        if (items !is MutableList<RecyclerViewItem>) {
            return
        }

        (items as MutableList<RecyclerViewItem>).removeAt(position)
        notifyItemRemoved(position)
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
